package com.example.yangchengyu.mylibrary.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yangchengyu.mylibrary.R;

/**
 * Created by YangChengyu on 2017/5/11.
 */

public class SplashFragment extends Fragment {

    private AppCompatTextView sectionLabel;
    private AppCompatTextView sectionIntro;
    private ImageView sectionImg;

    private int page;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public SplashFragment() {
    }

    public static SplashFragment newInstance(int sectionNumber) {
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);

        initViews(rootView);

        switch (page) {
            case 1:
                sectionImg.setBackgroundResource(R.drawable.ic_camera_black_24dp);
                sectionLabel.setText(R.string.onboarding_section_2);
                sectionIntro.setText(R.string.onboarding_intro_2);
                break;
            case 2:
                sectionImg.setBackgroundResource(R.drawable.ic_notifications_black_24dp);
                sectionLabel.setText(R.string.onboarding_section_3);
                sectionIntro.setText(R.string.onboarding_intro_3);
                break;
            case 3:
                sectionImg.setBackgroundResource(R.drawable.ic_beenhere_black_24dp);
                sectionLabel.setText(R.string.onboarding_section_1);
                sectionIntro.setText(R.string.onboarding_intro_1);
                break;
            default:
                break;
        }
        return rootView;
    }

    private void initViews(View view) {
        sectionLabel = (AppCompatTextView) view.findViewById(R.id.section_label);
        sectionIntro = (AppCompatTextView) view.findViewById(R.id.section_intro);
        sectionImg = (ImageView) view.findViewById(R.id.section_img);
    }
}