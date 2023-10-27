package com.baidu.baidumaps.oemdata;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "mydb";
    public static String Table = "tb";
    public static int VERSION_CODE = 1;
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_CODE);
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //首次创建时的回调
        //创建字段
        //sql   ：  create table table_name(_id integer,name varchar,age integer, salary integer)
        String sql = "create table "+Table+"(_id integer,home_set_status varchar,home_set_status_v varchar,company_set_status varchar,company_set_status_v varchar)";
        db.execSQL(sql);
        String sql2 =  "INSERT INTO "+Table+" (_id,home_set_status,home_set_status_v,company_set_status,company_set_status_v) VALUES (1, 'home_set_status', 'true','company_set_status','true')";
        db.execSQL(sql2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
