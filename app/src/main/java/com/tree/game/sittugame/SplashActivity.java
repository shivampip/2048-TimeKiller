package com.tree.game.sittugame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private final static long DELAY= 2100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Timer timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent mainIn= new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIn);
                finish();
            }
        }, DELAY);


    }




}
