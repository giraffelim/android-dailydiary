package com.example.admin.dailydiary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //singleton
    private static DBHelper instance;

    public static DBHelper getInstance(Context context){
        if(instance == null){
            instance = new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(Context context) {
        //context,DB이름
        //DB가 존재하면 오픈, 없으면 생성해서 오픈
        super(context, "diaryDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table diaryDB(dNo integer primary key autoincrement, date varchar(11), img varchar(20), contents varchar(200) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}