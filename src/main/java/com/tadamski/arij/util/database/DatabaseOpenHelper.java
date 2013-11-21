package com.tadamski.arij.util.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.googlecode.androidannotations.annotations.EBean;

/**
 * Created by Ilya on 11/20/13.
 *
 * Creates the database structure when the database is first created.
 */
@EBean
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    public static final String FAVORITES_TABLE_NAME = "favorites";
    private static final String FAVORITES_TABLE_CREATE =
            "CREATE TABLE " + FAVORITES_TABLE_NAME + " (issue_key TEXT PRIMARY KEY);";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FAVORITES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
