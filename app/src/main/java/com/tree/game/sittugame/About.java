package com.tree.game.sittugame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);

        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");

        Element devElement = new Element();
        devElement.setGravity(Gravity.CENTER);
        //devElement.setColor(Color.DKGRAY);
        devElement.setTitle(this.getString(R.string.developedBy));

        try {
            String version = BuildConfig.VERSION_NAME;
            versionElement.setTitle("Version " + version);
        } catch (Exception e) {
        }


        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.icon)
                .setDescription(getResources().getString(R.string.description))
                .addItem(versionElement)
                .addGroup("Connect with us")
                .addEmail(getResources().getString(R.string.email))
                .addFacebook(getResources().getString(R.string.facebookId))
                .addPlayStore(this.getPackageName())
                .addItem(getCopyRightsElement())
                .addItem(devElement)
                .create();

        this.setContentView(aboutPage);


    }//onCreateEND

    private Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        //copyRightsElement.setIcon(R.drawable.about_icon_copy_right);
        //copyRightsElement.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display("Copyright");
            }
        });
        return copyRightsElement;
    }

    private void display(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}//classEND
