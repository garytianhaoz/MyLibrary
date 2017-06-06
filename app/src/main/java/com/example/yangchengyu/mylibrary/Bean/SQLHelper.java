package com.example.yangchengyu.mylibrary.Bean;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by YangChengyu on 2017/5/10.
 */

public class SQLHelper extends SQLiteOpenHelper {

    public static final String DataBaseName = "myBook";
    public static final int Version = 1;
    public static final String TableName = "bookInfo";

    private Context mContext;

    public SQLHelper(Context context) {
        super(context, DataBaseName, null, Version);
        mContext = context;
    }


    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Create_Table = "CREATE TABLE IF NOT EXISTS " + TableName + "("
                + "id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,"
                + "title TEXT,"
                + "author TEXT,"
                + "publisher TEXT,"
                + "pubdate DATE,"
                + "summary TEXT,"
                + "image TEXT"
                + ")";

        try {
            db.execSQL(Create_Table);
            Toast.makeText(mContext, "Create Table", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
