package com.example.usuario.paolo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BdHelper extends SQLiteOpenHelper
{
    String sqlCreate = "CREATE TABLE TotalVenta (_id INTEGER PRIMARY KEY AUTOINCREMENT, idventa TEXT, cliente TEXT, total TEXT, entregado TEXT, fecha DATE DEFAULT CURRENT_TIME)";
    String sqlCreate1 = "CREATE TABLE Venta (_id INTEGER PRIMARY KEY AUTOINCREMENT, idventa TEXT, cantidad TEXT, producto TEXT, unitario TEXT, importe TEXT, idprod TEXT, sto TEXT,unit TEXT)";
    String sqlCreate2 = "CREATE TABLE Precios (_id INTEGER PRIMARY KEY AUTOINCREMENT, idprod TEXT, producto TEXT, stock TEXT, unitario TEXT, especial TEXT)";
    String sqlCreate3 = "CREATE TABLE Acuenta (_id INTEGER PRIMARY KEY AUTOINCREMENT, cliente TEXT, serie TEXT, total TEXT, acuenta NUMERIC(10,2))";

    public BdHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt)
    {
        super(paramContext, paramString, paramCursorFactory, paramInt);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
        paramSQLiteDatabase.execSQL(this.sqlCreate);
        paramSQLiteDatabase.execSQL(this.sqlCreate1);
        paramSQLiteDatabase.execSQL(this.sqlCreate2);
        paramSQLiteDatabase.execSQL(this.sqlCreate3);
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
    }
}