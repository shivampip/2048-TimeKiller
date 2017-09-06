package com.tree.game.sittugame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;

import java.util.StringTokenizer;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;
    FButton contiFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        contiFB = (FButton) findViewById(R.id.contiFB);

        MobileAds.initialize(this, getResources().getString(R.string.app_id));
        sp = PreferenceManager.getDefaultSharedPreferences(this);


        ShivConfig shivConfig = new ShivConfig(this);
        shivConfig.start();

    }//onCreateEND

    public boolean isContinue() {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        int pn = sp.getInt("noOfBlock", 0);
        int pnumber = sp.getInt("number", 0);

        int n = sp.getInt(Game.BACKUP_KEY_N, 0);
        int number = sp.getInt(Game.BACKUP_KEY_NUMBER, 0);
        String data = sp.getString(Game.BACKUP_KEY, null);
        if (data != null && n == pn && number == pnumber) {
            return true;
        } else {
            return false;
        }
    }

    public void newGame(View v) {
        Intent i = new Intent(this, Game.class);
        i.putExtra("continue", false);
        this.startActivity(i);
    }


    public void continueGame(View v) {
        boolean isNew = sp.getBoolean(Game.NEWGAME_SP, false);
        if (isNew) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(Game.NEWGAME_SP, false);
            editor.apply();
        }

        Intent i = new Intent(this, Game.class);
        i.putExtra("continue", !isNew);
        this.startActivity(i);
    }


    public void setting(View v) {
        Intent i = new Intent(this, Setting.class);
        this.startActivity(i);
    }


    public void about(View v) {
        Intent i = new Intent(this, About.class);
        this.startActivity(i);
    }


    public void exit(View v) {
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isContinue()) {
            contiFB.setVisibility(View.VISIBLE);
        } else {
            contiFB.setVisibility(View.GONE);
        }
    }

    public void invite(View v) {
        String link = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
        String shareBody = getString(R.string.shareText) + "\n" + link;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share"));
    }

    public void display(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
