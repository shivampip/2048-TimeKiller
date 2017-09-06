package com.tree.game.sittugame;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Sittu Agrawal on 24-05-2017.
 */

class Const {


    public static void showWinDialog(Context context, int best){
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout= inflater.inflate(R.layout.win_dialog, null);
        final AlertDialog ad[]= new AlertDialog[1];
        AlertDialog.Builder builder= new AlertDialog.Builder(context);

        TextView tv = (TextView) layout.findViewById(R.id.winDiaTv);
        tv.setText("Congratulation, You just built "+best+" tile. That's amazing. keep it up.....");
        Button continueB= (Button) layout.findViewById(R.id.winDiaB);
        continueB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad[0].dismiss();
            }
        });
        builder.setView(layout);
        builder.setCancelable(false);
        ad[0]= builder.create();
        ad[0].show();
    }

    public static void showLostDialog(Context context,int score, final View.OnClickListener newGame, final View.OnClickListener exit){
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout= inflater.inflate(R.layout.lost_dialog, null);
        final AlertDialog ad[]= new AlertDialog[1];
        AlertDialog.Builder builder= new AlertDialog.Builder(context);

        TextView scroeTv= (TextView) layout.findViewById(R.id.lostDiaScroeTv);
        scroeTv.setText("Your Score:-  "+score);
        Button newGameB= (Button) layout.findViewById(R.id.lostDiaNewB);
        final Button exitB= (Button) layout.findViewById(R.id.lostDiaExitB);
        newGameB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad[0].dismiss();
                newGame.onClick(view);
            }
        });
        exitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad[0].dismiss();
                exit.onClick(view);
            }
        });
        builder.setView(layout);
        builder.setCancelable(false);
        ad[0]= builder.create();
        ad[0].show();
    }


}//classEND

