package com.bjuw.twnln.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bjuw.twnln.R;
import com.bjuw.twnln.adapter.ThemeActivityAdapter;
import com.bjuw.twnln.pre.AppLicationPreferences;

public class ThemeActivity extends Activity implements OnItemClickListener {

    AppLicationPreferences appLicationPreferences;
    private GridView listView_themes_list;
    private ThemeActivityAdapter themeActivityAdapter;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        appLicationPreferences = AppLicationPreferences.getInstance(this);

        initView();
    }


    private void initView() {
        themeActivityAdapter = new ThemeActivityAdapter(getApplicationContext());
        listView_themes_list = (GridView) findViewById(R.id.listView_themes_list);
        back = (ImageButton) findViewById(R.id.activity_theme_back);
        listView_themes_list.setAdapter(themeActivityAdapter);
        listView_themes_list.setOnItemClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        appLicationPreferences.setKeyBoardThemeId(position + 1);
        Toast.makeText(ThemeActivity.this,"The theme is set up successfully",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
