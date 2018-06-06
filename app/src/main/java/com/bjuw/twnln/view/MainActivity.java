package com.bjuw.twnln.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjuw.twnln.R;
import com.bjuw.twnln.pre.AppLicationPreferences;
import com.bjuw.twnln.utils.d;
import com.soundcloud.android.crop.Crop;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity implements OnClickListener {
    private static final String TAG = "MainActivity";
    public static final int CHOOSE_PHOTO = 2;
    AppLicationPreferences appLicationPreferences;
    ImageButton set_a, set_b, set_theme;
    private TextView main_text;
    private int defulte_tag = 0;
    boolean isShow = false;
    private RelativeLayout main_bg;
    private String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appLicationPreferences = AppLicationPreferences.getInstance(this);

        initView();

        new d().d1(this);

        getAllMothod();
        getOpenInputMothod();

    }

    private void initView() {
        main_bg = (RelativeLayout) findViewById(R.id.main_bg);
        set_a = (ImageButton) findViewById(R.id.set_a);
        set_b = (ImageButton) findViewById(R.id.set_b);
        set_theme = (ImageButton) findViewById(R.id.set_theme);
        main_text = (TextView) findViewById(R.id.main_text);
        set_a.setOnClickListener(this);
        set_b.setOnClickListener(this);
        set_theme.setOnClickListener(this);
        findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                } else {
//                    openAlbum();
//                }
                Crop.pickImage(MainActivity.this);
            }
        });
    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You have rejected this permission", Toast.LENGTH_SHORT).show();
                }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_a:
                Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                startActivityForResult(intent, 1);
                break;
            case R.id.set_b:
                if (defulte_tag == 0) {
                    Toast.makeText(this, R.string.please_first, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.please_choose, Toast.LENGTH_SHORT).show();
                    ((InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            while (!isShow) {
                                String defaultIme = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
                                Log.i(TAG, "---------defaultIme------------: " + defaultIme);
                                if (defaultIme.equals("com.bjuw.twnln/.keyboard.DIYKeyboardService")) {
                                    isShow = true;
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isShow) {
                                        set_b.setBackgroundResource(R.drawable.ic_spep2_click);
//                                        set_b.setText("第二步：设置成功");
                                        set_b.setEnabled(false);

//                                        rl_a.setVisibility(View.GONE);
//                                        rl_b.setVisibility(View.VISIBLE);
                                        set_a.setVisibility(View.GONE);
                                        set_b.setVisibility(View.GONE);
                                        main_text.setText("");
                                        main_bg.setBackgroundResource(R.drawable.ic_main_bg_true);
                                    }
                                }
                            });


                        }
                    }).start();


                }
                break;

            case R.id.set_theme:

                startActivity(new Intent(MainActivity.this, ThemeActivity.class));

                break;
        }
    }


    private void getAllMothod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> methodList = imm.getEnabledInputMethodList();

        int tag = 0;
        for (InputMethodInfo mi : methodList) {
            CharSequence name = mi.loadLabel(getPackageManager());
            String s = name.toString();
            Log.i(TAG, "----------输入法名字-------------" + s);
            if (s.equals("DIY Keyboard")) {
                tag = 1;
                set_a.setBackgroundResource(R.drawable.ic_spep1_click);
                main_text.setText("");
//                set_a.setText("第一步：输入法已启用");
                set_a.setEnabled(false);
                defulte_tag = 1;
            } else if (tag == 0) {
                set_a.setBackgroundResource(R.drawable.ic_spep1);
//                set_a.setText("第一步：去启用输入法");

                set_b.setBackgroundResource(R.drawable.ic_spep2);

                defulte_tag = 0;
            }
        }
    }

    private void getOpenInputMothod() {
        String defaultIme = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        Log.i(TAG, "---------defaultIme------------: " + defaultIme);
        if (defaultIme.equals("com.bjuw.twnln/.keyboard.DIYKeyboardService") && defulte_tag == 1) {
            set_b.setBackgroundResource(R.drawable.ic_spep2_click);
//            set_b.setText("第二步：设置成功");
            set_b.setEnabled(false);
//            rl_a.setVisibility(View.GONE);
//            rl_b.setVisibility(View.VISIBLE);
            set_a.setVisibility(View.GONE);
            set_b.setVisibility(View.GONE);
            main_text.setText("");
            main_bg.setBackgroundResource(R.drawable.ic_main_bg_true);
        } else {
//            rl_a.setVisibility(View.VISIBLE);
//            rl_b.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                getAllMothod();
                getOpenInputMothod();
                break;
            case 9162:
                // start() 方法根据其的需求选择不同的重载方法
                if (data != null) {
                    Log.i(TAG, "onActivityResult: ---data.getData()-----  " + data.getData());
                    Log.i(TAG, "onActivityResult: ---createImagePathUri(this)-----  " + createImagePathUri(this));
                    Crop.of(data.getData(), createImagePathUri(this)).withAspect(540, 384).start(this);
                } else {

                }

                break;
            case Crop.REQUEST_CROP:
                if (data != null && Crop.getOutput(data) != null) {
                    Intent intent = new Intent(this, Cut2Activity.class);
                    Log.i(TAG, "onActivityResult: ---Crop.getOutput(data)-----  " + Crop.getOutput(data));
                    Log.i(TAG, "onActivityResult: ---Crop.getOutput(data).toString -----  " + Crop.getOutput(data).toString());
                    intent.putExtra("imagepath", Crop.getOutput(data).toString());
                    startActivity(intent);
                }
                break;

            case Crop.RESULT_ERROR:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

        }

    }

    private static Uri createImagePathUri(Context context) {
        Uri imageFilePath = null;
        String status = Environment.getExternalStorageState();
        SimpleDateFormat timeFormatter = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
            imageFilePath = context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            imageFilePath = context.getContentResolver().insert(
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        }
        Log.i("", "生成的照片输出路径：" + imageFilePath.toString());
        return imageFilePath;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {

        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                Uri uri1 = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getPath(uri1, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getPath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }

    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getPath(uri, null);
    }

    private String getPath(Uri uri, String selection) {
        String path = null;
        Cursor query = getContentResolver().query(uri, null, selection, null, null);
        if (query != null) {
            if (query.moveToFirst()) {
                path = query.getString(query.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            query.close();
        }
        return path;
    }

}
