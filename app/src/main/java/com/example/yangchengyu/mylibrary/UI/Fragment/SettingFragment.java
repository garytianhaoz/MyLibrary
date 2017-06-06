package com.example.yangchengyu.mylibrary.UI.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yangchengyu.mylibrary.R;
import com.example.yangchengyu.mylibrary.UI.activity.SettingActivity;
import com.example.yangchengyu.mylibrary.UI.Adapter.SettingAdapter;
import com.example.yangchengyu.mylibrary.UI.activity.AboutActivity;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

/**
 * Created by YangChengyu on 2017/5/15.
 */

public class SettingFragment extends Fragment {

    private View mRootView;
    private RollPagerView mRollPagerView;
    private TextView mTextView_setting;
    private TextView mTextView_about;

    public static SettingFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString("tag", tag);
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SettingFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_setting, container, false);

        mRollPagerView = (RollPagerView) mRootView.findViewById(R.id.roll_view_pager);
        mRollPagerView.setPlayDelay(5000);
        mRollPagerView.setAnimationDurtion(1000);
        mRollPagerView.setHintView(new ColorPointHintView(getContext(), Color.YELLOW, Color.WHITE));
        mRollPagerView.setAdapter(new SettingAdapter());

        mTextView_setting = (TextView) mRootView.findViewById(R.id.setting_textView_setting);
        mTextView_about = (TextView) mRootView.findViewById(R.id.setting_textView_about);

        mTextView_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutActivity.class));
            }
        });

        mTextView_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });


        return mRootView;
    }
}
