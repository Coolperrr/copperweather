package com.copperweather.android.copperweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.copperweather.android.R;

/**
 * Created by copper on 2017/1/17.
 */

public class DialogActivity extends AppCompatActivity {

    private ImageView cancelImage;
    private TextView mAddTextView, mSettingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        mAddTextView = (TextView) findViewById(R.id.add_delete);
        mAddTextView.setText("添加或删除城市");
        mAddTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DialogActivity.this,AddCityActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mSettingTextView = (TextView) findViewById(R.id.setting);
        mSettingTextView.setText("设置");

        cancelImage = (ImageView) findViewById(R.id.cancel);
        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
