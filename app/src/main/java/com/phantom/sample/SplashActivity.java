package com.phantom.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SplashActivity extends BaseActivity implements Runnable {

    private ImageView mLogoImageView;
    private FloatingActionButton mFloatingActionButton;
    private ImageView mTextImageView;

    @Override
    protected void init() {
        mLogoImageView = findViewById(R.id.splash_logo_iv);
        mFloatingActionButton = findViewById(R.id.floating_btn);
        mTextImageView = findViewById(R.id.splash_text);
        mFloatingActionButton.postDelayed(this, 400);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    private void enterLoginOrHome() {
        SharedPreferences sp = getSharedPreferences("phantom", Context.MODE_PRIVATE);
        String account = sp.getString("userInfo", null);
        if (account == null) {
            startActivity(LoginActivity.class);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("userInfo", account);
            startActivity(MainActivity.class, bundle);
        }
        finish();
    }

    @Override
    public void run() {
        final View parentView = (View) mFloatingActionButton.getParent();
        float scale = (float) (Math.sqrt(parentView.getHeight() * parentView.getHeight() + parentView.getWidth()
                * parentView.getWidth()) / mFloatingActionButton.getHeight());
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", scale);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", scale);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mFloatingActionButton, scaleX, scaleY).setDuration(500);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                parentView.setBackgroundColor(SplashActivity.this.getResources().getColor(R.color.white));
                mFloatingActionButton.setVisibility(View.GONE);
                mLogoImageView.setVisibility(View.VISIBLE);
                mTextImageView.setVisibility(View.VISIBLE);
            }
        });
        PropertyValuesHolder holderA = PropertyValuesHolder.ofFloat("alpha", 0, 1);
        PropertyValuesHolder holderYm = PropertyValuesHolder.ofFloat("translationY", 0, 150);
        ObjectAnimator textAnimator = ObjectAnimator.ofPropertyValuesHolder(mLogoImageView, holderA, holderYm).setDuration(700);
        textAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        textAnimator.setStartDelay(500);

        textAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                enterLoginOrHome();
            }
        });
        objectAnimator.start();
        textAnimator.start();

    }
}
