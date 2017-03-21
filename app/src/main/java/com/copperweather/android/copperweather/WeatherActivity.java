package com.copperweather.android.copperweather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.copperweather.android.R;
import com.copperweather.android.db.CityChosen;
import com.copperweather.android.snowfall.FlakeView;
import com.copperweather.android.util.ZoomOutPageTransformer;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private ViewPager mPager;
    private MyPagerAdapter mAdapter;
    private FrameLayout mFrameLayout;
    private FlakeView flakeView;
    private Handler handlerRain = new Handler();
    public List<WeatherFragment> fragmentList = new ArrayList<>();
    public List<CityChosen> weatherIdList = new ArrayList<>();
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        //从数据库里获取数据
        weatherIdList = DataSupport.findAll(CityChosen.class);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        flakeView = new FlakeView(this);
        mFrameLayout.addView(flakeView);

        if (!weatherIdList.isEmpty()) {
            for (int i = 0;i<weatherIdList.size();i++){
                fragmentList.add(WeatherFragment.newInstance(weatherIdList.get(i).getWeatherId()));
                Log.i("tag", "onCreate: "+weatherIdList.get(i).getWeatherId()+"\n"+fragmentList.get(i).toString());
            }
            Log.i("tag", "onCreate: "+weatherIdList.size());
        }

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setPageTransformer(true,new ZoomOutPageTransformer());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent()!=null){
            intent = getIntent();
            if(intent.getStringExtra("flag")=="add"){
                weatherIdList = DataSupport.findAll(CityChosen.class);
                addNewItem();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        handlerRain.postDelayed(runnableRain, 1000);
    }

    private Runnable runnableRain = new Runnable() {
        @Override
        public void run() {
            flakeView.addFlakes(15);
            handlerRain.postDelayed(runnableRain, 4000);
            if (flakeView.getNumFlakes() > 75) {
                handlerRain.removeCallbacks(runnableRain);
            }
        }
    };

    public void addNewItem() {
        mAdapter.notifyDataSetChanged();
    }

    public void removeCurrentItem() {
        int position = mPager.getCurrentItem();
        fragmentList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public WeatherFragment getItem(int position) {
            Log.i("tag", "onCreate: "+position);
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return MyPagerAdapter.POSITION_NONE;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
