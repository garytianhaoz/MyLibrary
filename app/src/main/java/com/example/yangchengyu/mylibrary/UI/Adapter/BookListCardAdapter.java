package com.example.yangchengyu.mylibrary.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yangchengyu.mylibrary.Bean.BookInfo;
import com.example.yangchengyu.mylibrary.Listener.OnItemClickListener;
import com.example.yangchengyu.mylibrary.Listener.OnItemLongClickListener;
import com.example.yangchengyu.mylibrary.R;

import java.util.List;

/**
 * Created by YangChengyu on 2017/5/6.
 */

public class BookListCardAdapter extends RecyclerView.Adapter<BookListCardAdapter.BookHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    public static String TAG = BookListCardAdapter.class.getSimpleName();

    private Context context;
    private List<BookInfo> mBookInfoList;
    private OnItemClickListener mItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;

    public BookListCardAdapter(Context context, List<BookInfo> bookInfoList) {
        this.context = context;
        this.mBookInfoList = bookInfoList;
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

    static class BookHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView book_title;
        public TextView book_info;
        public TextView book_introduce;
        private final CardView mCardView;

        public BookHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.item_book_img);
            book_title = (TextView) itemView.findViewById(R.id.item_book_title);
            book_info = (TextView) itemView.findViewById(R.id.item_book_info);
            book_introduce = (TextView) itemView.findViewById(R.id.item_book_introduce);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_book_list, parent, false);
        BookHolder bookHolder = new BookHolder(v);
        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
        Log.i(TAG, "布局成功！");
        return bookHolder;
    }

    @Override
    public void onBindViewHolder(BookHolder holder, final int position) {
        int i = position;
        holder.book_title.setText(mBookInfoList.get(i).getTitle());
        holder.book_info.setText(mBookInfoList.get(i).getInfoString());
        holder.book_introduce.setText(mBookInfoList.get(i).getSummary());
        Glide.with(context).load(mBookInfoList.get(i).getImage()).into(holder.mImageView);
        holder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return mBookInfoList.size();
    }

}
