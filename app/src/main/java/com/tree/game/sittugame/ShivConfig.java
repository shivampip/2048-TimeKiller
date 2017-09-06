package com.tree.game.sittugame;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Sittu Agrawal on 12-05-2017.
 */

class ShivConfig {

//    private Context context;
//    private SharedPreferences sp;
//    private FirebaseRemoteConfig mConfig;


    public ShivConfig(Context context) {
//        this.context = context;
//        sp = PreferenceManager.getDefaultSharedPreferences(context);
//        mConfig = FirebaseRemoteConfig.getInstance();
//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setDeveloperModeEnabled(BuildConfig.DEBUG)
//                .build();
//        mConfig.setConfigSettings(configSettings);
//        mConfig.setDefaults(R.xml.remote_config_defaults);
    }

    public void start() {
//        long cacheExpiration = 3600; // 1 hour in seconds.
//        if (mConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
//            cacheExpiration = 0;
//        }
//
//        mConfig.fetch(cacheExpiration)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            display("Succeed");
//                            mConfig.activateFetched();
//                        } else {
//                            display("Failed");
//                        }
//                        createWelcomeDialog();
//                    }
//                });
    }


//    private boolean isDisplayed(String sno) {
//        return sp.getBoolean(sno, false);
//    }
//
//    private void setDisplayed(String sno) {
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean(sno, true);
//        editor.apply();
//    }
//
//
//    private final String SNO = "sno";
//    private final String HEADING = "heading";
//    private final String DESCRIPTION = "description";
//    private final String BTN_TXT = "btnTxt";
//    private final String IMG_URL = "imgUrl";
//    private final String BACKCOLOR= "backColor";
//
//    private void createWelcomeDialog() {
//        final String sno = mConfig.getString(SNO);
//        if (isDisplayed(sno)) {
//            return;
//        }
//        String heading = mConfig.getString(HEADING);
//        String description = mConfig.getString(DESCRIPTION);
//        String btnTxt = mConfig.getString(BTN_TXT);
//        String imgUrl = mConfig.getString(IMG_URL);
//        String colStr= mConfig.getString(BACKCOLOR);
//
//        final AlertDialog ad[] = new AlertDialog[1];
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View layout= inflater.inflate(R.layout.welcome_dialog, null);
//
//        LinearLayout rootLl= (LinearLayout) layout;
//        TextView titleTv= (TextView) layout.findViewById(R.id.wdTitleTv);
//        TextView descTv= (TextView) layout.findViewById(R.id.wdDescTv);
//        Button btn= (Button) layout.findViewById(R.id.wdButtonB);
//        ImageView imgV= (ImageView) layout.findViewById(R.id.wdImageIv);
//
//        titleTv.setText(heading);
//        descTv.setText(description);
//        Glide.with(context).load(imgUrl).into(imgV);
//        rootLl.setBackgroundColor(Color.parseColor(colStr));
//        btn.setText(btnTxt);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ad[0].dismiss();
//                setDisplayed(sno);
//            }
//        });
//        builder.setView(layout);
//        builder.setCancelable(false);
//
//
//        ad[0] = builder.create();
//        ad[0].show();
//    }


    private void display(String msg) {
        //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


}//classEND
