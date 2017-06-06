package com.example.yangchengyu.mylibrary.UI.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.yangchengyu.mylibrary.UI.Fragment.SplashFragment;

/**
 * Created by YangChengyu on 2017/5/11.
 */

public class SplashAdapter extends FragmentPagerAdapter {

    private final int pageCount = 3;

    public SplashAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return SplashFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return pageCount;
    }

}