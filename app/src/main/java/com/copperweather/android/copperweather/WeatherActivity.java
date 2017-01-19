package com.copperweather.android.copperweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import com.copperweather.android.snowfall.FlakeView;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private ViewPager mPager;
    private MyPagerAdapter mAdapter;
    private FrameLayout mFrameLayout;
    private FlakeView flakeView;
    private Handler handlerRain = new Handler();
    public List<WeatherFragment> fragmentList = new ArrayList<>();
    public List<String> weatherIdList = new ArrayList<>();
    public static boolean flag = false;
    public static int count =0;
    SharedPreferences prefs;
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

        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        mPager = (ViewPager) findViewById(R.id.viewpager);
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        flakeView = new FlakeView(this);
        mFrameLayout.addView(flakeView);


        if (fragmentList.size() >= 0) {
            for (int i = 0;i<=prefs.getInt("countId",0);i++){
                weatherIdList.add(prefs.getString("weather"+i,null));
                Log.i("TAG", "fragmentList " + fragmentList.size() + "count " +
                        mPager.getCurrentItem()+"weatherList"+weatherIdList.toString());
                fragmentList.add(WeatherFragment.newInstance(weatherIdList.get(i),i));
            }
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putInt("fragmentListSize", fragmentList.size());
            editor.apply();
        }


        mAdapter = new MyPagerAdapter(this.getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flag){
            addNewItem();
        }
        Log.i("TAG", "fragmentList " + fragmentList.size() + "count " +
                mPager.getCurrentItem()+"weatherList"+weatherIdList.toString());
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
//        weatherIdList.add(prefs.getString("weather"+count,null));
//        fragmentList.add(WeatherFragment.newInstance(weatherIdList.get(mPager.getCurrentItem())));
        mAdapter.notifyDataSetChanged();
        flag = false;
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
        public Fragment getItem(int position) {
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
