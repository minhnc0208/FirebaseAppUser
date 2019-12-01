package com.ncm.nguyencaominh.firebaseappuser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Splash_Srceen_Activity extends Activity {
    private ImageView ivs;
    private ProgressBar progressBar ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ivs= findViewById(R.id.ivs);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.anima) ;
        ivs.startAnimation(myanim);
        final Intent i = new Intent(this, MainActivity.class);
        CountDownTimer countDownTimer = new CountDownTimer(5000,5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }.start();
    }
}
