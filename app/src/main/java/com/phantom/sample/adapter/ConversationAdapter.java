package com.phantom.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.phantom.client.model.Conversation;
import com.phantom.sample.App;
import com.phantom.sample.R;
import com.phantom.sample.widget.GlideRoundTransform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ConversationAdapter extends BaseAdapter<Conversation, ConversationAdapter.ConversationHolder> {

    public ConversationAdapter(Context context, List<Conversation> data) {
        super(context, data);
    }

    @Override
    public void bind(ConversationHolder holder, int position) {
        Conversation conversation = mData.get(position);
        Glide.with(App.getContext())
                .load(getAvatar())
                .transform(new GlideRoundTransform(mContext, 4))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.ic_avatar_default)
                .into(holder.avatar);
        holder.badge.setText(String.valueOf(conversation.getUnread()));
        holder.message.setText(conversation.getLastMessage());
        holder.name.setText(conversation.getConversationName());
        holder.time.setText(getCurrentTime(convertTime(conversation.getLastUpdate())));

    }

    @NonNull
    @Override
    public ConversationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_conversation, parent, false);
        return new ConversationHolder(view);
    }

    public void addFirst(Conversation conversation) {
        mData.add(conversation);
    }

    public void update(Conversation conversation) {
        for (Conversation c : mData) {
            if (conversation.getConversationId() == c.getConversationId()) {
                c.setLastMessage(conversation.getLastMessage());
                c.setUnread(conversation.getUnread());
                c.setLastUpdate(conversation.getLastUpdate());
            }
        }
        Collections.sort(mData, (o1, o2) -> (int) (o2.getLastUpdate() - o1.getLastUpdate()));
    }


    static final class ConversationHolder extends RecyclerView.ViewHolder {

        ImageView avatar;

        TextView badge;

        TextView name;

        TextView time;

        TextView message;

        ConversationHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.conversation_avatar);
            badge = itemView.findViewById(R.id.conversation_badge);
            name = itemView.findViewById(R.id.conversation_name);
            time = itemView.findViewById(R.id.conversation_time);
            message = itemView.findViewById(R.id.conversation_message);
        }
    }

    private String getAvatar() {
        return "http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg";
    }


    /**
     * 格式化时间
     *
     * @param time 2015-10-02 11:30
     * @return 11:30 | 昨天 11:：30   10/2
     */
    private String getCurrentTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        if (time == null || "".equals(time)) {
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    //今天
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        Calendar yesterday = Calendar.getInstance();    //昨天
        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        current.setTime(date);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);
        int hours = current.get(Calendar.HOUR_OF_DAY);
        int minute = current.get(Calendar.MINUTE);
        if (current.after(today)) {
            return String.format(Locale.getDefault(), "%d:%s", hours,
                    minute >= 10 ? String.valueOf(minute) :
                            String.format(Locale.getDefault(), "0%d", minute));
        } else if (current.before(today) && current.after(yesterday)) {
            return String.format(Locale.getDefault(), "昨天 %d:%s", hours,
                    minute >= 10 ? String.valueOf(minute) :
                            String.format(Locale.getDefault(), "0%d", minute));
        } else {
            return String.format(Locale.getDefault(), "%d月%d日", month + 1, day);
        }
    }

    /**
     * 时间戳转换时间格式
     *
     * @param timestamp 时间戳
     * @return 时间格式
     */
    public static String convertTime(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(new Date(timestamp));
    }

}
