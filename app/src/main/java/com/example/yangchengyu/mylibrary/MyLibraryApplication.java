package com.example.yangchengyu.mylibrary;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.request.target.ViewTarget;

/**
 * Created by YangChengyu on 2017/5/11.
 */

public class MyLibraryApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MyLibraryApplication.sContext = getApplicationContext();
        ViewTarget.setTagId(R.id.glide_tag);
    }

    public static Context getContext() {
        return MyLibraryApplication.sContext;
    }
}
