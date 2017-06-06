package com.example.yangchengyu.mylibrary.UI.Fragment;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.yangchengyu.mylibrary.Bean.BookInfo;
import com.example.yangchengyu.mylibrary.Bean.DataKeeper;
import com.example.yangchengyu.mylibrary.Listener.OnItemClickListener;
import com.example.yangchengyu.mylibrary.Listener.OnItemLongClickListener;
import com.example.yangchengyu.mylibrary.R;
import com.example.yangchengyu.mylibrary.UI.Adapter.BookListCardAdapter;
import com.example.yangchengyu.mylibrary.UI.activity.DetailActivity;
import com.example.yangchengyu.mylibrary.Utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangChengyu on 2017/5/4.
 */

public class BookListCardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mRootView;
    private RecyclerView mRecyclerView;
    private List<BookInfo> mBookInfos;
    private boolean isScroll = true;
    private DataKeeper mDataKeeper;
    private BookListCardAdapter mBookListCardAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private PopupWindow mPopupWindow;
    private int longClickPosition;
    public static final String Book_URL = "http://192.168.43.250/BookDemo/getBookJSON.php";

    public static BookListCardFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString("tag", tag);
        BookListCardFragment fragment = new BookListCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BookListCardFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.recycle_content_card, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                JSONUtils.getBook(Book_URL, getBookHandle);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            Boolean isScrolling = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isScroll) {
                    int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                    int totalItemCount = mLinearLayoutManager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1)) {

                        mBookListCardAdapter.notifyDataSetChanged();
                        isScroll = false;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    isScrolling = true;
                } else {
                    isScrolling = false;
                }
            }
        });

        mBookInfos = new ArrayList<>();

        mBookListCardAdapter = new BookListCardAdapter(getContext(), mBookInfos);
        mBookListCardAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookInfo bookInfo = mBookInfos.get(position);
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("title", bookInfo.getTitle());
                intent.putExtra("info", bookInfo.getInfoString());
                intent.putExtra("introduce", bookInfo.getSummary());
                intent.putExtra("image", bookInfo.getImage());

                intent.putExtra("author", bookInfo.getAuthor());
                intent.putExtra("pubdate", bookInfo.getPubdate());
                intent.putExtra("publisher", bookInfo.getPublisher());
                startActivity(intent);
            }
        });

        mBookListCardAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {

            private TextView mPopUpTextView;

            @Override
            public boolean onItemLongClick(View view, int position) {
                longClickPosition = position;
                if (null == mPopupWindow) {
                    View popUpWindowView = inflater.inflate(R.layout.dialog_long_click, null);
                    mPopUpTextView = (TextView) popUpWindowView.findViewById(R.id.dialog_long_click_textView);
                    mPopupWindow = new PopupWindow(popUpWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mPopupWindow.setAnimationStyle(R.style.PopAnimStyle);
                    mPopupWindow.setOutsideTouchable(true);
                    mPopUpTextView.setText("Add to MyLibrary");
                    mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                    mPopUpTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDataKeeper = new DataKeeper(getContext());
                            BookInfo bookInfo = mBookInfos.get(longClickPosition);
                            mDataKeeper.saveBook(bookInfo);
                            if (mPopupWindow.isShowing())
                                mPopupWindow.dismiss();
                        }
                    });
                }
                if (mPopupWindow.isShowing())
                    mPopupWindow.dismiss();
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                mPopupWindow.showAtLocation(view, Gravity.TOP, location[0], location[1]);
                return true;
            }
        });

//        mLinearLayoutManager.setStackFromEnd(true);
//        mLinearLayoutManager.setReverseLayout(true);
        mRecyclerView.setAdapter(mBookListCardAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        return mRootView;
    }

    private Handler getBookHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String JsonData = (String) msg.obj;
            try {
                mBookInfos.clear();
                JSONArray jsonArray = new JSONArray(JsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    String author = jsonObject.getString("author");
                    String publisher = jsonObject.getString("publisher");
                    String pubdate = jsonObject.getString("pubdate");
                    String summary = jsonObject.getString("summary");
                    String image = jsonObject.getString("image");
                    mBookInfos.add(new BookInfo(title, author, publisher, pubdate, summary, image));
                }
                mBookListCardAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JSONUtils.getBook(Book_URL, getBookHandle);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}


