package com.bjuw.twnln.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bjuw.twnln.R;
import com.bjuw.twnln.pre.AppLicationPreferences;

import java.io.FileNotFoundException;

public class Cut2Activity extends AppCompatActivity {

    private ImageView iv;
    private ImageButton btn,btn_b;
    private String imagepath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut2);

        iv = (ImageView) findViewById(R.id.iv_cut2activity);
        btn = (ImageButton) findViewById(R.id.btn_cut2activity);
        btn_b = (ImageButton) findViewById(R.id.btn_b_cut2activity);
        findViewById(R.id.activity_cut2_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cut2Activity.this,MainActivity.class));
                finish();
            }
        });

        Intent intent = getIntent();
        imagepath = intent.getStringExtra("imagepath");
        Uri parse = Uri.parse(imagepath);
//        iv.setImageBitmap(BitmapFactory.decodeFile(imagepath));
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(parse));
            iv.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mybg = getSharedPreferences("mybg", 0);
                SharedPreferences.Editor edit = mybg.edit();
                if (!imagepath.equals("")) {
                    edit.putString("mybg_path", imagepath);
                    edit.commit();

                    AppLicationPreferences.getInstance(Cut2Activity.this).setKeyBoardThemeId(100);
                    Toast.makeText(Cut2Activity.this,"The theme is set up successfully",Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });

        btn_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cut2Activity.this,MainActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Cut2Activity.this,MainActivity.class));
        finish();
    }
}
