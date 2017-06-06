package com.example.yangchengyu.mylibrary.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.example.yangchengyu.mylibrary.Bean.BookInfo;
import com.example.yangchengyu.mylibrary.Listener.OnItemClickListener;
import com.example.yangchengyu.mylibrary.Listener.OnItemLongClickListener;
import com.example.yangchengyu.mylibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangChengyu on 2017/5/10.
 */

public class MyLibraryAdapter extends RecyclerView.Adapter<MyLibraryAdapter.MyLibraryHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    public static String TAG = MyLibraryAdapter.class.getSimpleName();

    private Context context;
    private List<BookInfo> mBookInfoList;
    private OnItemClickListener mItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;

    public MyLibraryAdapter() {

    }

    public MyLibraryAdapter(Context context, ArrayList<BookInfo> bookInfoList) {
        this.context = context;
        mBookInfoList = bookInfoList;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return mOnItemLongClickListener.onItemLongClick(v, (int) v.getTag());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.mOnItemLongClickListener = longClickListener;
    }



    static class MyLibraryHolder extends RecyclerView.ViewHolder {

        private final ImageButton mImageButton;

        public MyLibraryHolder(View itemView) {
            super(itemView);
            mImageButton = (ImageButton) itemView.findViewById(R.id.grid_image_button);
        }
    }

    @Override
    public MyLibraryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_book_grid, parent, false);
        MyLibraryHolder myLibraryHolder = new MyLibraryHolder(v);
        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
        Log.i(TAG, "布局成功！");
        return myLibraryHolder;
    }

    @Override
    public void onBindViewHolder(MyLibraryHolder holder, int position) {
        int i = position;
        Glide.with(context).load(mBookInfoList.get(i).getImage()).into(holder.mImageButton);
        holder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return mBookInfoList.size();
    }
}
