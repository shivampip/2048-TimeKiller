package com.tree.game.sittugame;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.util.StringTokenizer;

public class Game extends AppCompatActivity {


    private final int def_no_of_block = 4;
    private final int def_number = 2;
    private int number = 2;
    private int n;
    private int pBest = 0;
    private int textSize;
    private float windowBorderW;
    private final int UNDO_LEVEL = 3;
    private GameData sam;
    private final GameData[] psam = new GameData[UNDO_LEVEL];
    private SharedPreferences sp;
    private Resources rs;

    private int height;
    private int width, mWidth;
    private RelativeLayout screen;
    private TableLayout gameArea;
    private TextView[][] tv;
    private TextView bestScore;
    private TextView totalScore;
    private TextView bsHead;
    private TextView targetTv;
    //ImageView gameAreaBack;
    //ImageView toolBack;
    private AdView adView;
    private InterstitialAd mInterAd;
    private RelativeLayout gameRl;
    private Button undoB;
    private Button breakB, newGameB;
    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);


        rs = this.getResources();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        typeface = Setting.getCustomTypeface(this);

        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        width = Resources.getSystem().getDisplayMetrics().widthPixels;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mWidth = (int) (displayMetrics.widthPixels / displayMetrics.density);


        screen = (RelativeLayout) findViewById(R.id.activity_game);
        gameArea = (TableLayout) findViewById(R.id.tl);
        bestScore = (TextView) findViewById(R.id.bestScore);
        totalScore = (TextView) findViewById(R.id.totalScore);
        //gameAreaBack = (ImageView) findViewById(R.id.gameBack);
        gameRl = (RelativeLayout) findViewById(R.id.activity_game);
        bsHead = (TextView) findViewById(R.id.bshead);
        adView = (AdView) findViewById(R.id.avGame);
        undoB = (Button) findViewById(R.id.undoB);
        breakB = (Button) findViewById(R.id.breakB);
        targetTv = (TextView) findViewById(R.id.targetTv);
        newGameB= (Button) findViewById(R.id.newGameB);

        int pColor= getResources().getColor(R.color.colorPrimary);
        undoB.getBackground().setColorFilter(pColor, PorterDuff.Mode.MULTIPLY);
        breakB.getBackground().setColorFilter(pColor, PorterDuff.Mode.MULTIPLY);
        newGameB.getBackground().setColorFilter(pColor, PorterDuff.Mode.MULTIPLY);


        Intent intent = this.getIntent();
        boolean isConti = intent.getBooleanExtra("continue", false);

        //display("isConti is " + isConti);

        isNew = true;
        init();

        if (isConti) {
            backup();
        } else {
            setTargetAchieved(false);
        }

        initAds();

        setBackground();

    }//onCreateEND


    private void setBackground() {
        String type = Setting.getBType(this);
        //display("Type is " + type);
        switch (type) {
            case Setting.TYPE_COLOR:
                gameRl.setBackgroundColor(Setting.getBColor(this));
                break;
            case Setting.TYPE_IMAGE:
                Uri path = Setting.getBImage(this);
                File f = new File(getRealPathFromURI(path));
                Drawable d = Drawable.createFromPath(f.getAbsolutePath());
                gameRl.setBackground(d);
                break;
            default:
                gameRl.setBackgroundResource(R.drawable.wood_small);
                break;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void initAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //Interstitial Ad
        undoB.setVisibility(View.GONE);
        breakB.setVisibility(View.GONE);
        mInterAd = new InterstitialAd(this);
        mInterAd.setAdUnitId(getResources().getString(R.string.interstitial_button));
        mInterAd.loadAd(new AdRequest.Builder().build());
        mInterAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                undoB.setVisibility(View.VISIBLE);
                breakB.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                undoB.setVisibility(View.GONE);
                breakB.setVisibility(View.GONE);
                mInterAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void showInter() {
        if (mInterAd.isLoaded()) {
            mInterAd.show();
        }
    }


    private boolean isNew;

    private void init() {

        setNoOfBlock();

        sam = new GameData(n, number);
        for (int u = 0; u < UNDO_LEVEL; u++) {
            psam[u] = new GameData(n, number);
        }

        if (isNew) {
            isNew = false;
            screen.setOnTouchListener(touchListener);
            windowBorderW = rs.getDimension(R.dimen.windowBorderW) + rs.getDimension(R.dimen.frameBorderW);
            width = (int) (width - windowBorderW * 2);
            textSize = (mWidth / n) / 4;
        }

        tv = new TextView[n][n];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                tv[i][k] = new TextView(this);
            }
        }

        for (int i = 0; i < n; i++) {
            TableRow tr = new TableRow(this);
            tr.setGravity(Gravity.CENTER);

            for (int k = 0; k < n; k++) {
                tv[i][k].setBackgroundResource(R.drawable.block_def);
                tv[i][k].setHeight(width / n);
                tv[i][k].setWidth(width / n);
                tv[i][k].setTextColor(Color.WHITE);
                tv[i][k].setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                tv[i][k].setTextSize(textSize);
                tv[i][k].setMaxLines(1);
                tv[i][k].setTypeface(typeface);
                tr.addView(tv[i][k]);
            }

            gameArea.addView(tr);
        }

        //gameAreaBack.getLayoutParams().height= width;

        //bestScore.setText(0 + "");
        setTarget();
        totalScore.setText(0 + "");
        pBest = getBestScore();
        bestScore.setText(pBest + "");

        //gameAreaBack.getLayoutParams().height = width;

    }//initEND

    private void setTarget() {
        final int target = sam.getTarget();
        targetTv.setText(target + "");
        float textSize = getResources().getDimension(R.dimen.scoreTS);
        if (target < 10000) {
            targetTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        } else if (target < 100000) {
            targetTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 4 / 5);
        } else if (target < 1000000) {
            targetTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 3 / 4);
        } else if (target < 1000000) {
            targetTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 2 / 3);
        } else if (target < 10000000) {
            targetTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 4 / 7);
        } else if (target < 10000000) {
            targetTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 3 / 6);
        } else if (target < 100000000) {
            targetTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 3 / 7);
        } else if (target < 100000000) {
            targetTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 3 / 8);
        }
        targetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDisplay("Target is " + target);
            }
        });
        //setColor(targetTv, target);
    }

    private void setNoOfBlock() {
        n = sp.getInt("noOfBlock", def_no_of_block);
        number = sp.getInt("number", def_number);
        SharedPreferences.Editor editor= sp.edit();
        editor.putInt("noOfBlock", n);
        editor.putInt("number", number);
        editor.apply();
    }


    //Bug in UNDO FIX IT..................$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    private void storePChal() {
        if (cUndoLevel < UNDO_LEVEL - 1) {
            cUndoLevel++;
        }
        for (int i = 0; i < sam.n; i++) {
            for (int k = 0; k < sam.n; k++) {
                int u = 0;
                for (u = 0; u < UNDO_LEVEL - 1; u++) {
                    psam[u].a[i][k] = psam[u + 1].a[i][k];
                }
                psam[u].a[i][k] = sam.a[i][k];
            }
        }
    }

    private void chal(String dir) {
        storePChal();
        switch (dir) {
            case "up":
                sam.slide("up");
                sam.adding("up");
                sam.slide("up");
                sam.show();
                sam.random();
                update();
                break;

            case "down":
                sam.slide("down");
                sam.adding("down");
                sam.slide("down");
                sam.random();
                sam.show();
                update();
                break;

            case "left":
                sam.slide("left");
                sam.adding("left");
                sam.slide("left");
                sam.random();
                sam.show();
                update();
                break;

            case "right":
                sam.slide("right");
                sam.adding("right");
                sam.slide("right");
                sam.random();
                sam.show();
                update();
                break;
        }
    }


    private void top(View v) {
        chal("up");
    }

    private void bottom(View v) {
        chal("down");
    }

    private void left(View v) {
        chal("left");
    }

    private void right(View v) {
        chal("right");
    }


    private void update() {
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                setColor(tv[i][k], sam.a[i][k]);
                if (sam.a[i][k] < 1000) {
                    tv[i][k].setTextSize(textSize);
                } else if (sam.a[i][k] < 10000) {
                    tv[i][k].setTextSize(textSize * 4 / 5);
                } else if (sam.a[i][k] < 100000) {
                    tv[i][k].setTextSize(textSize * 3 / 4);
                } else if (sam.a[i][k] < 100000) {
                    tv[i][k].setTextSize(textSize * 2 / 3);
                } else if (sam.a[i][k] < 1000000) {
                    tv[i][k].setTextSize(textSize * 4 / 7);
                } else if (sam.a[i][k] < 1000000) {
                    tv[i][k].setTextSize(textSize * 3 / 6);
                } else if (sam.a[i][k] < 10000000) {
                    tv[i][k].setTextSize(textSize * 3 / 7);
                } else if (sam.a[i][k] < 10000000) {
                    tv[i][k].setTextSize(textSize * 3 / 8);
                }
            }
        }
        //bestScore.setText(sam.getBest() + "");
        totalScore.setText(sam.getScore() + "");
        if (sam.getScore() > pBest) {
            pBest = sam.getScore();
            bestScore.setText(pBest + "");
            setBestScore(pBest);
        }
        updateBackground();

        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        GameData vgd = new GameData(sam.n, sam.number);
        for (int i = 0; i < n; i++) {
            System.arraycopy(sam.a[i], 0, vgd.a[i], 0, n);
        }
        IsMovePossible imp = new IsMovePossible();
        imp.execute(vgd);
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        if (sam.getBest() == sam.getTarget() && (!isTargetAchieved())) {
            Const.showWinDialog(this, sam.getBest());
            setTargetAchieved(true);
        }

    }

    private void setTargetAchieved(boolean isAchieved) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("targetAchieved", isAchieved);
        editor.apply();
    }

    private boolean isTargetAchieved() {
        return sp.getBoolean("targetAchieved", false);
    }


    private void updateBackground() {
        //int col = rs.getColor(colors[sam.getLevel()]);
        //gameAreaBack.setBackgroundColor(col);
        //gameAreaBack.setAlpha(0.25f);
    }

    int colors[] = {
            R.color.col2,
            R.color.col4,
            R.color.col8,
            R.color.col16,
            R.color.col32,
            R.color.col64,
            R.color.col128,
            R.color.col256,
            R.color.col512,
            R.color.col1024,
            R.color.col2048,
            R.color.col4096,
            R.color.col8192,
            R.color.col16384,
            R.color.col32768
    };


    private void setColor(TextView tv, int value) {
        tv.setText(value + "");
        int lev = sam.getLevel(value);

        if (value == 0) {
            tv.setText("");
            tv.setBackgroundResource(R.drawable.block_def);
            return;
        }

        switch (lev) {
            case 0:
                tv.setBackgroundResource(R.drawable.block2);
                break;
            case 1:
                tv.setBackgroundResource(R.drawable.block4);
                break;
            case 2:
                tv.setBackgroundResource(R.drawable.block8);
                break;
            case 3:
                tv.setBackgroundResource(R.drawable.block16);
                break;
            case 4:
                tv.setBackgroundResource(R.drawable.block32);
                break;
            case 5:
                tv.setBackgroundResource(R.drawable.block64);
                break;
            case 6:
                tv.setBackgroundResource(R.drawable.block128);
                break;
            case 7:
                tv.setBackgroundResource(R.drawable.block256);
                break;
            case 8:
                tv.setBackgroundResource(R.drawable.block512);
                break;
            case 9:
                tv.setBackgroundResource(R.drawable.block1024);
                break;
            case 10:
                tv.setBackgroundResource(R.drawable.block2048);
                break;
            case 11:
                tv.setBackgroundResource(R.drawable.block4096);
                break;
            case 12:
                tv.setBackgroundResource(R.drawable.block8192);
                break;
            case 13:
                tv.setBackgroundResource(R.drawable.block16384);
                break;
            case 14:
                tv.setBackgroundResource(R.drawable.block32768);
                break;
            default:
                tv.setBackgroundResource(R.drawable.block32768);
                break;
        }

    }


    private final float FILING_DISTANCE = 50;
    private final View.OnTouchListener touchListener = new View.OnTouchListener() {
        float downX, downY, upX, upY, fX, fY;
        boolean tookIn = false;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {


            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                downX = motionEvent.getX();
                downY = motionEvent.getY();
                tookIn = false;
            }
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE) {
                fX = motionEvent.getX();
                fY = motionEvent.getY();
                float dx = downX - fX;
                dx = (dx < 0) ? (dx * -1) : dx;

                float dy = downY - fY;
                dy = (dy < 0) ? (dy * -1) : dy;


                if ((dx > FILING_DISTANCE || dy > FILING_DISTANCE) && !tookIn) {
                    tookIn = true;
                    logic();
                }
            }
            return true;
        }

        private void logic() {
            float dX = fX - downX;
            float dY = fY - downY;

            if (dX > 0 && isBig(dX, dY)) {
                right(null);
            } else if (dX < 0 && isBig(dX, dY)) {
                left(null);
            } else if (dY > 0 && isBig(dY, dX)) {
                bottom(null);
            } else if (dY < 0 && isBig(dY, dX)) {
                top(null);
            }
        }

        private boolean isBig(float a, float b) {
            if (a < 0) a = a * -1;
            if (b < 0) b = b * -1;
            return a > b;
        }
    };


    //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    final static String NEWGAME_SP = "new_game";
    public static final String BACKUP_KEY = "game_data_backup";
    public static final String BACKUP_KEY_N = "no_of_blocks_backup";
    public static final String BACKUP_KEY_NUMBER = "number_backup";
    public static final String BACKUP_BEST_SCORE = "best_score";
    public static final String BACKUP_SCORE = "score";

    private void save() {
        String data = "";
        for (int i = 0; i < sam.n; i++) {
            for (int j = 0; j < sam.n; j++) {
                data += sam.a[i][j] + "$";
            }
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(BACKUP_KEY, data);
        editor.putInt(BACKUP_KEY_N, sam.n);
        editor.putInt(BACKUP_KEY_NUMBER, sam.number);
        editor.putInt(BACKUP_SCORE, sam.getScore());
        editor.putInt(BACKUP_BEST_SCORE, sam.getBest());
        editor.apply();
    }

    private void backup() {
        //("Value is " + sp.getString(BACKUP_KEY, "NO"));

        int n = sp.getInt(BACKUP_KEY_N, 0);
        int number = sp.getInt(BACKUP_KEY_NUMBER, 0);
        String data = sp.getString(BACKUP_KEY, null);
        if (data != null && n == this.n && number == this.number) {
            StringTokenizer st = new StringTokenizer(data, "$");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int a = Integer.parseInt(st.nextToken());
                    sam.a[i][j] = a;
                }
            }
            sam.setBest(sp.getInt(BACKUP_BEST_SCORE, 0));
            sam.setScore(sp.getInt(BACKUP_SCORE, 0));
            update();
        }
    }

    private final static String BEST_SCORE_PRE = "bestScoreFor";

    private int getBestScore() {
        return sp.getInt(BEST_SCORE_PRE + n + "and" + number, 0);
    }

    private void setBestScore(int score) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(BEST_SCORE_PRE + n + "and" + number, score);
        editor.apply();
    }
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$44444

    public void setting(View v) {
        Intent i = new Intent(this, Setting.class);
        this.startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
        if (adView != null) {
            adView.destroy();
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
        if (adView != null) {
            adView.pause();
        }
        //finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }


    private class IsMovePossible extends AsyncTask<GameData, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(GameData... data) {
            GameData gd = data[0];
            if (gd.slide("up")) {
                return false;
            }
            if (gd.slide("down")) {
                return false;
            }
            if (gd.slide("left")) {
                return false;
            }
            if (gd.slide("right")) {
                return false;
            }
            if (gd.adding("up")) {
                return false;
            }
            if (gd.adding("down")) {
                return false;
            }
            if (gd.adding("left")) {
                return false;
            }
            if (gd.adding("right")) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                gameOver();
            }
        }

    }


    private void gameOver() {
        //display("Game Over.");

        Const.showLostDialog(this,sam.getScore(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTargetAchieved(false);
                gameArea.removeAllViews();
                init();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(NEWGAME_SP, true);
                editor.apply();
                finish();
            }
        });
    }


    public void startNewGame(View v) {
        final AlertDialog ad[] = new AlertDialog[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Game");
        builder.setMessage("Are you sure want to start New Game.\nYour current progress will lost.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ad[0].dismiss();
                setTargetAchieved(false);
                gameArea.removeAllViews();
                init();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ad[0].dismiss();
            }
        });

        ad[0] = builder.create();
        ad[0].setCancelable(false);
        ad[0].show();
    }

    private int cUndoLevel = UNDO_LEVEL - 1;

    public void undo(View v) {
        //customDisplay("UNDO");
        if (cUndoLevel < 0) {
            return;
        }
        int sum = 0;
        for (int p = 0; p < n; p++) {
            for (int q = 0; q < n; q++) {
                sum += psam[cUndoLevel].a[p][q];
            }
        }
        if (sum == 0) {
            return;
        }
        for (int i = 0; i < n; i++) {
            System.arraycopy(psam[cUndoLevel].a[i], 0, sam.a[i], 0, n);
        }
        update();
        cUndoLevel--;
        showInter();
    }


    public void breakATile(final View v) {
        customDisplay("Select a block to break it.");
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                tv[i][k].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int p = 0; p < n; p++) {
                            for (int q = 0; q < n; q++) {
                                if (view == tv[p][q]) {
                                    if (sam.a[p][q] == 0) {
                                        customDisplay("Empty block .Try again...");
                                        return;
                                    } else {
                                        sam.a[p][q] = 0;
                                        update();
                                        reset();
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }//breakEND

    private void reset() {
        save();
        gameArea.removeAllViews();
        init();
        backup();
        showInter();
        //cUndoLevel= -1;
    }

    private void customDisplay(String msg) {
        Toast tt = new Toast(this);
        LayoutInflater inflator = this.getLayoutInflater();
        View vv = inflator.inflate(R.layout.custom_toast_layout, null);
        TextView cTv = (TextView) vv.findViewById(R.id.cTv);
        cTv.setText(msg);
        vv.requestLayout();
        tt.setView(vv);
        tt.setGravity(Gravity.CENTER, 0, 0);
        tt.show();
    }

    public void display(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}//classEND
