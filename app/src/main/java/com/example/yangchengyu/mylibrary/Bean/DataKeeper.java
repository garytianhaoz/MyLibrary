package com.example.yangchengyu.mylibrary.Bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by YangChengyu on 2017/5/10.
 */

public class DataKeeper {

    private SQLHelper mSQLHelper;
    private SQLiteDatabase mDatabase;
    private Context mContext;


    public DataKeeper() {

    }

    public DataKeeper(Context context) {
        this.mSQLHelper = new SQLHelper(context);
        mContext = context;
    }

    public void saveBook(BookInfo bookInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", bookInfo.getTitle());
        contentValues.put("author", bookInfo.getAuthor());
        contentValues.put("publisher", bookInfo.getPublisher());
        contentValues.put("pubdate", bookInfo.getPubdate());
        contentValues.put("summary", bookInfo.getSummary());
        contentValues.put("image", bookInfo.getImage());
        Cursor cursor = null;

        mDatabase = mSQLHelper.getWritableDatabase();
        cursor = mDatabase.rawQuery("SELECT * FROM " + SQLHelper.TableName +
                " WHERE title=?", new String[]{bookInfo.getTitle()});
        if (!cursor.moveToNext()) {
            mDatabase.insert(SQLHelper.TableName, null, contentValues);
            Toast.makeText(mContext,"添加成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext,"已添加",Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        mDatabase.close();
    }

    public void deleteBook(BookInfo bookInfo) {
        mDatabase = mSQLHelper.getWritableDatabase();
        mDatabase.delete(SQLHelper.TableName, "title = ? ", new String[]{bookInfo.getTitle()});
        mDatabase.close();
        Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT).show();
    }

    public ArrayList<BookInfo> getAllBooks() {
        ArrayList<BookInfo> bookInfoList = new ArrayList<>();

        mDatabase = mSQLHelper.getWritableDatabase();
        Cursor cursor = mDatabase.rawQuery("Select * from " + SQLHelper.TableName, null);
        while (cursor.moveToNext()) {
            BookInfo bookInfo = new BookInfo();
            bookInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            bookInfo.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            bookInfo.setPubdate(cursor.getString(cursor.getColumnIndex("pubdate")));
            bookInfo.setPublisher(cursor.getString(cursor.getColumnIndex("publisher")));
            bookInfo.setSummary(cursor.getString(cursor.getColumnIndex("summary")));
            bookInfo.setImage(cursor.getString(cursor.getColumnIndex("image")));
            bookInfoList.add(bookInfo);
        }
        Toast.makeText(mContext,"获取成功",Toast.LENGTH_SHORT).show();
        cursor.close();
        mSQLHelper.close();
        return bookInfoList;
    }
}
