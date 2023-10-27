package com.baidu.baidumaps.oemdata;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class OemContentProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        DatabaseHelper helper = new DatabaseHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();


//        Log.i("OEMPROCVIDERTESTING", uri.toString());
//        Log.i("OEMPROCVIDERTESTING", projection[0]);
        if (uri.toString().contains("common_addr")) {


            String col =projection[0];
//        Log.i("OEMPROCVIDERTESTING", projection[0]);
            Cursor query = db.rawQuery("SELECT _id,"+col+","+col+"_v"+" FROM " + DatabaseHelper.Table, null);
            if (query != null) {
                while (query.moveToNext()) {
                    String a = query.getString(1);
                    String b = query.getString(2);
                    Log.i("OEMPROCVIDERTESTING", " 1:"+a);
                    Log.i("OEMPROCVIDERTESTING", " 2:"+b);
                }
            }
            return query;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
