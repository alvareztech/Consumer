package com.huawei.hiaicodedemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.Toast;

import com.huawei.hiai.vision.common.VisionBase;

import java.io.File;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public abstract class BaseActivity extends AppCompatActivity {
    public static final int REQUEST_PHOTO = 100;
    public static final int REQUEST_CAMERA = 101;
    public static final int REQUEST_CODE = 102;
    public static final int REQUEST_SELECT_MATERIAL_CODE = 103;
    public static final String KEY_FILE_PATH = "filePath";
    public static final String KEY_DIR_PATH = "dirPath";
    public Context mContext;
    public Uri photoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());
        mContext = this;
        setLanguage();
        init();
    }

    protected abstract void init();

    protected abstract int layout();

    public void goTo(boolean isClose, Class clazz) {
        Intent intent = new Intent(mContext, clazz);
        startActivity(intent);
        if (isClose) {
            finish();
        }
    }

    public void takePhoto() {

        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/temp");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }

        File img = new File(tmpDir.getAbsolutePath() + "/test.png");

        if (Build.VERSION.SDK_INT >= 24) {

            photoUri = FileProvider.getUriForFile(mContext, "com.huawei.hiaicodedemo", img);

        } else {
            photoUri = Uri.fromFile(img);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void selectImage() {
        //Intent intent = new Intent("android.intent.actionBar.GET_CONTENT");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PHOTO);

    }

    public void toast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void actionBar(String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setLanguage(){
        Resources resources = getApplication().getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if(isCN()){
            config.setLocale(Locale.SIMPLIFIED_CHINESE);
        }else {
            config.setLocale(Locale.ENGLISH);
        }
        resources.updateConfiguration(config,dm);

    }

    public boolean isCN() {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh")){
            return true;
        }else {
            return false;
        }
    }

    public void selectMaterial(String dirPath){
        Intent intent = new Intent(mContext, MaterialActivity.class);
        intent.putExtra(KEY_DIR_PATH,dirPath);
        startActivityForResult(intent,REQUEST_SELECT_MATERIAL_CODE);
    }

    public void releaseEngine(VisionBase visionBase) {
        if (visionBase != null) {
            visionBase.release();
        }
    }
}
