package com.tree.game.sittugame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import info.hoang8f.widget.FButton;

public class Setting extends AppCompatActivity {

    private SeekBar no_of_block;
    private SeekBar number;
    private SharedPreferences sp;

    private FButton selectColorFB;

    private int selectedColor;
    private ColorPickerDialog colorDialog;
    private ImageView demoIv;
    private TextView noOfBlockTv;
    private TextView numberTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        no_of_block = (SeekBar) findViewById(R.id.noOfBlock);
        number = (SeekBar) findViewById(R.id.number);
        noOfBlockTv = (TextView) findViewById(R.id.noOfBlockTv);
        numberTv = (TextView) findViewById(R.id.numberTv);
        selectColorFB = (FButton) findViewById(R.id.selectColorFB);
        demoIv = (ImageView) findViewById(R.id.demoIv);


        no_of_block.setMax(noOfBlock.length - 1);
        number.setMax(num.length - 1);

        no_of_block.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
        number.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));


        no_of_block.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                saveNoOfBlock(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        number.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                saveNumber(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setDefault();
        setBackground();

        int[] mColors = getResources().getIntArray(R.array.color_pallet);

        colorDialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                mColors,
                getBColor(this),
                4, // Number of columns
                ColorPickerDialog.SIZE_SMALL,
                true, // True or False to enable or disable the serpentine effect
                1, // stroke width
                Color.BLACK // stroke color
        );

        colorDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                selectedColor = color;
                //demoIv.setImageDrawable(null);
                //demoIv.setBackgroundColor(selectedColor);
                demoIv.setImageDrawable(new ColorDrawable(selectedColor));
                saveColor(selectedColor);
            }

        });


    }//onCreateEND


    private void setDefault() {
        int nob = sp.getInt("noOfBlock", 4);
        int n = sp.getInt("number", 2);
        noOfBlockTv.setText("Number of Block - " + nob);
        numberTv.setText("Initial Number - " + n);

        for (int i = 0; i < noOfBlock.length; i++) {
            if (nob == noOfBlock[i]) {
                no_of_block.setProgress(i);
            }
        }

        for (int i = 0; i < num.length; i++) {
            if (n == num[i]) {
                number.setProgress(i);
            }
        }
    }

    private void setBackground() {
        String type = Setting.getBType(this);
        //display("Type is " + type);
        switch (type) {
            case Setting.TYPE_COLOR:
                demoIv.setImageDrawable(new ColorDrawable(getBColor(this)));
                break;
            case Setting.TYPE_IMAGE:
                Uri path = Setting.getBImage(this);
                demoIv.setImageURI(path);
                break;
            default:
                demoIv.setBackgroundResource(R.drawable.wood_small);
                break;
        }
    }

//    private String getRealPathFromURI(Uri contentURI) {
//        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            return contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            return cursor.getString(idx);
//        }
//    }

    private void saveBType(String type) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("backType", type);
        editor.apply();
    }

    public static final String TYPE_COLOR = "color";
    public static final String TYPE_IMAGE = "image";

    public static String getBType(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("backType", "default");
    }

    private void saveColor(int color) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("backgroundColor", color);
        editor.apply();
        saveBType(TYPE_COLOR);
    }

    private void saveBImage(Uri imgUri) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("backImage", imgUri.toString());
        editor.apply();
        saveBType(TYPE_IMAGE);
    }

    public static Uri getBImage(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String raw = sp.getString("backImage", "none");
        try {
            Uri imgUri = Uri.parse(raw);
            return imgUri;
        } catch (Exception e) {
            return null;
        }
    }


    public static int getBColor(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt("backgroundColor", Color.parseColor("#7f00ff"));
    }

    private void saveNoOfBlock(int number) {
        noOfBlockTv.setText("Number of Block - " + noOfBlock[number]);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("noOfBlock", noOfBlock[number]);
        editor.apply();
    }

    private void saveNumber(int number) {
        numberTv.setText("Initial Number - " + num[number]);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("number", num[number]);
        editor.apply();
    }


    private final int[] noOfBlock = {2,3, 4, 5, 6, 7};
    private final int[] num = {1, 2, 5, 10};

    private int getNoOfBlock(int level) {
        return noOfBlock[level];
    }

    private int getNum(int level) {
        return num[level];
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


    public void selectColor(View v) {
        colorDialog.show(getFragmentManager(), "color_dialog_test");
    }

    public void selectImage(View v) {
        browsImage();
    }

    private final static int GALLERY_CODE = 1231;

    private void browsImage() {
        Intent gellaryIn = new Intent(Intent.ACTION_GET_CONTENT);
        gellaryIn.setType("image/*");
        startActivityForResult(Intent.createChooser(gellaryIn, "Select Picture..."), GALLERY_CODE);
    }//browsImgEND

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY_CODE:
                if (data != null) {
                    Uri uri = data.getData();
                    cropImage(uri);
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri picUri = result.getUri();
                    saveBImage(picUri);
                    demoIv.setImageURI(picUri);
                    //demoIv.setBackgroundColor(Color.TRANSPARENT);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
        }
    }

    private void cropImage(Uri uri) {
        int xAR = 3, yAR = 5;
        CropImageView.CropShape cropShape = CropImageView.CropShape.RECTANGLE;

        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(cropShape)
                .setAspectRatio(xAR, yAR)
                .setBackgroundColor(Color.argb(100, 100, 100, 100))
                //.setRequestedSize(250, 250, CropImageView.RequestSizeOptions.RESIZE_FIT)
                .start(this);
    }


    public static Typeface getCustomTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/maven_bold.ttf");
    }


    public void setDefault(View v) {
        int nob = 4;
        int n = 2;
        noOfBlockTv.setText("Number of Block - " + nob);
        numberTv.setText("Initial Number - " + n);

        for (int i = 0; i < noOfBlock.length; i++) {
            if (nob == noOfBlock[i]) {
                no_of_block.setProgress(i);
            }
        }

        for (int i = 0; i < num.length; i++) {
            if (n == num[i]) {
                number.setProgress(i);
            }
        }

        demoIv.setImageResource(R.drawable.wood_small);
        saveBType("default");

    }

    public void display(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}//classEND
