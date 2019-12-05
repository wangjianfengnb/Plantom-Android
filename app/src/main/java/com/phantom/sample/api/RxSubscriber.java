package com.phantom.sample.api;

import android.app.Activity;
import android.content.Context;

import com.cazaea.sweetalert.SweetAlertDialog;

import rx.Subscriber;


public abstract class RxSubscriber<T> extends Subscriber<T> {

    private boolean isShowDialog;

    private Context mContext;

    private String mMessage;

    private SweetAlertDialog dialog;

    protected RxSubscriber(Context context) {
        this(context, true, null);
    }

    protected RxSubscriber(Context context, String message) {
        this(context, true, message);
    }

    protected RxSubscriber(Context context, boolean isShowDialog) {
        this(context, isShowDialog, null);
    }

    public RxSubscriber(Context context, boolean isShowDialog, String msg) {
        if (context == null) {
            throw new IllegalArgumentException("RxSubscriber context can't be null ! ");
        }
        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("RxSubscriber context mush be instanceof activity !");
        }
        this.mContext = context;
        this.mMessage = msg;
        this.isShowDialog = isShowDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isShowDialog) {
            dialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("加载中...");
            dialog.setCancelable(true);

            dialog.show();
            //点击取消的时候取消订阅
            dialog.setOnCancelListener(dialog -> {
                if (!isUnsubscribed()) {
                    unsubscribe();
                    onUnSubscribe();
                }
            });
            dialog.show();
        }
    }


    @Override
    public void onCompleted() {
        if (dialog != null && isShowDialog && dialog.isShowing()) {
            if (!((Activity) mContext).isFinishing()) {
                dialog.dismiss();
            }
        }
    }

    public void onUnSubscribe() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        _onError(e.getClass().getSimpleName());
        if (dialog != null && isShowDialog && dialog.isShowing()) {
            if (!((Activity) mContext).isFinishing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onNext(T o) {
        if (dialog != null && isShowDialog && dialog.isShowing()) {
            if (!((Activity) mContext).isFinishing()) {
                dialog.dismiss();
            }
        }
        _onNext(o);
    }

    protected abstract void _onError(String msg);

    protected abstract void _onNext(T data);
}
