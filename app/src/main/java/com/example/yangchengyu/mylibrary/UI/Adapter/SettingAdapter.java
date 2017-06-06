package com.example.yangchengyu.mylibrary.UI.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yangchengyu.mylibrary.R;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

/**
 * Created by YangChengyu on 2017/5/15.
 */

public class SettingAdapter extends StaticPagerAdapter {

    private int[] img = {R.drawable.p6, R.drawable.p5, R.drawable.p3, R.drawable.p4};


    @Override
    public View getView(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageResource(img[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public int getCount() {
        return img.length;
    }
}
