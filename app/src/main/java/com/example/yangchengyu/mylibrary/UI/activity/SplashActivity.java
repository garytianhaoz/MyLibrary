package com.example.yangchengyu.mylibrary.UI.activity;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.yangchengyu.mylibrary.R;
import com.example.yangchengyu.mylibrary.UI.Adapter.SplashAdapter;
import com.example.yangchengyu.mylibrary.Utils.SettingUtils;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private SplashAdapter mSplashAdapter;
    private ViewPager mViewPager;
    private AppCompatButton buttonFinish;
    private ImageButton buttonPre;
    private ImageButton buttonNext;
    private int currentPosition;
    private ImageView[] indicators;
    private int bgColors[];
    private SharedPreferences mSp;

    private static final int MSG_DATA_INSERT_FINISH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSp = PreferenceManager.getDefaultSharedPreferences(this);
        if (mSp.getBoolean(SettingUtils.KEY_FIRST_LAUNCH, true)) {

            initViews();
            initData();

            mViewPager.addOnPageChangeListener(mOnPageChangeListener);

            buttonFinish.setOnClickListener(this);
            buttonNext.setOnClickListener(this);
            buttonPre.setOnClickListener(this);

            handler.sendEmptyMessage(MSG_DATA_INSERT_FINISH);
        } else {
            navigateToMainActivity();
            finish();
        }
    }

    private void initViews() {
        mSplashAdapter = new SplashAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.splash_container);
        buttonFinish = (AppCompatButton) findViewById(R.id.buttonFinish);
        buttonFinish.setText(R.string.onboarding_finish_button_description_wait);
        buttonFinish.setEnabled(false);
        buttonNext = (ImageButton) findViewById(R.id.imageButtonNext);
        buttonPre = (ImageButton) findViewById(R.id.imageButtonPre);
        indicators = new ImageView[]{(ImageView) findViewById(R.id.imageViewIndicator0),
                (ImageView) findViewById(R.id.imageViewIndicator1),
                (ImageView) findViewById(R.id.imageViewIndicator2)};
        mViewPager.setAdapter(mSplashAdapter);
    }

    private void initData() {
        bgColors = new int[]{ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.recycler_color1),
                ContextCompat.getColor(this, R.color.recycler_color2)};
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DATA_INSERT_FINISH:
                    buttonFinish.setText(R.string.onboarding_finish_button_description);
                    buttonFinish.setEnabled(true);
                    break;
            }
        }
    };

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.onboarding_indicator_selected :
                            R.drawable.onboarding_indicator_unselected
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int colorUpdate = (Integer) new ArgbEvaluator().evaluate(positionOffset,
                    bgColors[position], bgColors[position == 2 ? position : position + 1]);
            mViewPager.setBackgroundColor(colorUpdate);
        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            updateIndicators(position);
            mViewPager.setBackgroundColor(bgColors[position]);
            buttonPre.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            buttonNext.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
            buttonFinish.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void navigateToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonFinish:
                SharedPreferences.Editor ed = mSp.edit();
                ed.putBoolean(SettingUtils.KEY_FIRST_LAUNCH, false);
                ed.apply();
                navigateToMainActivity();
                break;
            case R.id.imageButtonNext:
                currentPosition += 1;
                mViewPager.setCurrentItem(currentPosition, true);
                break;
            case R.id.imageButtonPre:
                currentPosition -= 1;
                mViewPager.setCurrentItem(currentPosition, true);
                break;
        }
    }
}
