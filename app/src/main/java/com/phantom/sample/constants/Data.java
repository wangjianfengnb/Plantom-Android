package com.phantom.sample.constants;

import com.phantom.sample.api.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Data {

    private static List<String> avatars = new ArrayList<>();

    public static UserResponse USER;

    static {
        avatars.add("http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg");
        avatars.add("https://ww1.sinaimg.cn/large/0065oQSqly1g2hekfwnd7j30sg0x4djy.jpg");
        avatars.add("https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg");
        avatars.add("https://ws1.sinaimg.cn/large/0065oQSqly1fytdr77urlj30sg10najf.jpg");
        avatars.add("https://ws1.sinaimg.cn/large/0065oQSqly1fymj13tnjmj30r60zf79k.jpg");
        avatars.add("https://ws1.sinaimg.cn/large/0065oQSqgy1fy58bi1wlgj30sg10hguu.jpg");
        avatars.add("https://ws1.sinaimg.cn/large/0065oQSqgy1fxno2dvxusj30sf10nqcm.jpg");
        avatars.add("https://ws1.sinaimg.cn/large/0065oQSqgy1fxd7vcz86nj30qo0ybqc1.jpg");
        avatars.add("https://ws1.sinaimg.cn/large/0065oQSqgy1fwyf0wr8hhj30ie0nhq6p.jpg");
        avatars.add("https://ws1.sinaimg.cn/large/0065oQSqgy1fwgzx8n1syj30sg15h7ew.jpg");
    }

    public static String getAvatar() {
        Random random = new Random();
        int i = random.nextInt(avatars.size());
        return avatars.get(i);
    }


}
