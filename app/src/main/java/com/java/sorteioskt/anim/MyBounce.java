package com.java.sorteioskt.anim;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.java.sorteioskt.R;

public class MyBounce implements Interpolator {

    private double myAmplitude = 1;
    private double myFrequency = 10;

    public MyBounce(double amplitude, double frequency){
        myAmplitude = amplitude;
        myFrequency = frequency;
    }

    @Override
    public float getInterpolation(float time){
        return (float)(-1* Math.pow(Math.E,-time/myAmplitude)* Math.cos(myFrequency*time)+1);
    }

    public static void animationBounce(View view, Context c) {
        final Animation animation = AnimationUtils.loadAnimation(c, R.anim.bounce);
        MyBounce interpolator = new MyBounce(0.05, 5);
        animation.setInterpolator(interpolator);
        view.startAnimation(animation);
    }

    public static void vibrar(Context c) {
        Vibrator vibrator = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
        long millissegundos = 40;
        vibrator.vibrate(millissegundos);
    }
}