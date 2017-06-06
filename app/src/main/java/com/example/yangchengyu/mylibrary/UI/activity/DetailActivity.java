package com.example.yangchengyu.mylibrary.UI.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yangchengyu.mylibrary.Bean.BookInfo;
import com.example.yangchengyu.mylibrary.Bean.DataKeeper;
import com.example.yangchengyu.mylibrary.Bean.SQLHelper;
import com.example.yangchengyu.mylibrary.R;

/**
 * Created by YangChengyu on 2017/5/6.
 */

public class DetailActivity extends AppCompatActivity {

    private DataKeeper mDataKeeper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_detail);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView imageView_img = (ImageView) findViewById(R.id.detail_book_img);
        ImageView imageView_bg = (ImageView) findViewById(R.id.detail_book_bg);
        TextView textView_info = (TextView) findViewById(R.id.detail_book_writer_publisher);
        TextView textView_introduce = (TextView) findViewById(R.id.detail_book_introduce);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.detail_floatingActionButton);

        final String title = getIntent().getStringExtra("title");
        String info = getIntent().getStringExtra("info");
        final String introduce = getIntent().getStringExtra("introduce");
        final String image = getIntent().getStringExtra("image");

        final String author = getIntent().getStringExtra("author");
        final String pubdate = getIntent().getStringExtra("pubdate");
        final String publisher = getIntent().getStringExtra("publisher");

        collapsingToolbarLayout.setTitle(title);
        textView_info.setText(info);
        textView_introduce.setText(introduce);
        Glide.with(this).load(image).into(imageView_bg);
        Glide.with(this).load(image).into(imageView_img);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataKeeper = new DataKeeper(v.getContext());
                mDataKeeper.saveBook(new BookInfo(title, author, publisher, pubdate, introduce, image));
            }
        });
    }
}
