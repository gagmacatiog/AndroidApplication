package com.example.georgealbert.queuemobileapplication;

import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import java.text.Format;
import java.util.concurrent.TimeUnit;

/**
 * Created by George Albert on 2/7/2018.
 */

public class WaitingTime {
    private boolean checker = false;
    public CountDownTimer cdt;

    public void startWaitingTime(final TextView TV, final int Time, final int seconds) {

        if(!checker) {
            final long totalTime = Time * 60000 + seconds * 1000;

                cdt = new CountDownTimer(totalTime+1000, 1000) {
                    public void onTick(final long millisUntilFinished) {
                        final String withHours;
                            if(millisUntilFinished > 60*60*1000){
                                withHours = String.format("%02d:%02d",
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)));
                            }else{
                                withHours = String.format("%02d:%02d",
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                            }

                        TV.setText("" + withHours);
                    }

                    public void onFinish() {
                        TV.setText("- - : - -");
                    }
                }.start();

        }

    }
}


