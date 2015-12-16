package com.example.rank.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 27/04/2015
 * Time: 17:13
 */
public class DBHandler extends SQLiteOpenHelper {

    public static final String USER_TABLE = "_id INTEGER primary key autoincrement, nome TEXT NOT NULL";
    public static final String USER_ROUND_TABLE = "_id INTEGER primary key autoincrement, idUsuario INTEGER NOT NULL, idRound INTEGER NOT NULL";
    public static final String ROUND_TABLE = "_id INTEGER primary key autoincrement, dataini LONG NOT NULL, datafim LONG NOT NULL, taxa REAL NOT NULL";
    public static final String LANCAMENTO_TABLE = "_id INTEGER primary key autoincrement, data LONG NOT NULL, valor REAL NOT NULL, buyin REAL NOT NULL, out REAL NOT NULL, idUsuario INTEGER NOT NULL, idRound INTEGER NOT NULL";
    private static final String DB_NAME = "db_valores";
    private static final int DB_VERSION = 1;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists usuario(" + USER_TABLE + ");");
        db.execSQL("create table if not exists round(" + ROUND_TABLE + ");");
        db.execSQL("create table if not exists lancamento(" + LANCAMENTO_TABLE + ");");
        db.execSQL("create table if not exists usuarioround(" + USER_ROUND_TABLE + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists usuario");
        db.execSQL("drop table if exists round");
        db.execSQL("drop table if exists lancamento");
        db.execSQL("drop table if exists usuarioround");
        onCreate(db);
    }
}
