package com.example.yangchengyu.mylibrary.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.yangchengyu.mylibrary.UI.Adapter.MyLibraryAdapter;
import com.example.yangchengyu.mylibrary.UI.activity.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangChengyu on 2017/5/10.
 */

public class MyLibraryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mRootView;
    private RecyclerView mRecyclerView;
    private List<BookInfo> mBookInfos;
    private GridLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private MyLibraryAdapter mMyLibraryAdapter;
    private DataKeeper mDataKeeper;
    private Context mContext;
    private PopupWindow mPopupWindow;
    private int longClickPosition;

    public MyLibraryFragment() {

    }

    public static MyLibraryFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString("tag", tag);
        MyLibraryFragment fragment = new MyLibraryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.recycle_content_grid, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_widget_grid);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4);
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                initMyLibrary();
//            }
//        });
        mLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView_grid);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 2 >= mLayoutManager.getItemCount()) {

                    mMyLibraryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        mBookInfos = new ArrayList<>();

        mMyLibraryAdapter = new MyLibraryAdapter(getContext(), (ArrayList<BookInfo>) mBookInfos);
        mMyLibraryAdapter.setOnItemClickListener(new OnItemClickListener() {
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


        mMyLibraryAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {

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
                    mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                    mPopUpTextView.setText("Delete");
                    mPopUpTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDataKeeper = new DataKeeper(getContext());
                            BookInfo bookInfo = mBookInfos.get(longClickPosition);
                            mDataKeeper.deleteBook(bookInfo);
                            mMyLibraryAdapter.notifyDataSetChanged();
                            initMyLibrary();
                            if (mPopupWindow.isShowing())
                                mPopupWindow.dismiss();
                        }
                    });
                }
                if (mPopupWindow.isShowing())
                    mPopupWindow.dismiss();
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - 100);
                return true;
            }

        });

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mMyLibraryAdapter);

        return mRootView;
    }

    private void initMyLibrary() {
        ArrayList<BookInfo> bookInfoArrayList = null;
        mDataKeeper = new DataKeeper(getContext());
        mBookInfos.clear();
        bookInfoArrayList = mDataKeeper.getAllBooks();
        if (bookInfoArrayList.size() > 0) {
            for (int i = 0; i < bookInfoArrayList.size(); i++) {
                BookInfo bookInfo = bookInfoArrayList.get(i);
                mBookInfos.add(bookInfo);
            }
            mMyLibraryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initMyLibrary();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
}
