package com.example.juansebastianquinayasguarin.pets;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    TextView mTvJoy, mTvIt, mTvA;
    private final int DURACION_SPLASH = 4500;


    private final int DURACION_JOY = 1000;
    private final int TIEMPO_DESPUES_APARECE_JOY = 1000;

    private final int DURACION_IT = 1000;
    private final int TIEMPO_DESPUES_APARECE_IT = 1500;

    private final int DURACION_A = 2000;
    private final int TIEMPO_DESPUES_APARECE_A = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mTvJoy = findViewById(R.id.tvjoy);
        mTvIt = findViewById(R.id.tvIt);
        mTvA = findViewById(R.id.tvA);

        // APARECE 'JOY'
        AlphaAnimation fadeInJoy = new AlphaAnimation(0.0f, 1.0f);
        fadeInJoy.setDuration(DURACION_JOY);
        fadeInJoy.setStartOffset(TIEMPO_DESPUES_APARECE_JOY);
        fadeInJoy.setFillAfter(true);
        fadeInJoy.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //APARECE IT
        AlphaAnimation fadeInIt = new AlphaAnimation(0.0f, 1.0f);
        fadeInIt.setDuration(DURACION_IT);
        fadeInIt.setStartOffset(TIEMPO_DESPUES_APARECE_IT);
        fadeInIt.setFillAfter(true);

        fadeInIt.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //APARECE A
        AlphaAnimation fadeInA = new AlphaAnimation(0.0f, 1.0f);
        fadeInA.setDuration(DURACION_IT);
        fadeInA.setStartOffset(TIEMPO_DESPUES_APARECE_A);
        fadeInA.setFillAfter(true);

        fadeInA.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        mTvJoy.startAnimation(fadeInJoy);
        mTvIt.startAnimation(fadeInIt);
        mTvA.startAnimation(fadeInA);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            };
        }, DURACION_SPLASH);
    }
}
