package com.example.randomitempicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DataBaseHelper";
    private static final String TABLE_NAME = "item_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "item";
    private static final String dropStatement = "DROP TABLE IF EXISTS  "+TABLE_NAME;

    public DataBaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+COL2+" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropStatement);
        onCreate(db);
    }
    public boolean addData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,item);

        Log.d(TAG, "addDta: Adding " + item + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null,contentValues);

        if (result ==-1) {
            return false;
        }else{
            return true;
        }
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public String getRandomItem() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME + " ORDER BY RANDOM() LIMIT 1;";
        Cursor data = db.rawQuery(query,null);
        data.moveToNext();
        return data.getString(1);
    }

    public void ChangeItem(String s, String selectedItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+TABLE_NAME+" SET "+COL2+" = '"+s+"' WHERE "+COL2+" = '"+selectedItem+"'";
        db.execSQL(query);

    }

    public void DeleteItem(String selectedItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+TABLE_NAME+" WHERE "+COL1+" in (SELECT "+COL1+" FROM "+TABLE_NAME+" WHERE "+COL2+" ='"+selectedItem+"' LIMIT 1)";
        db.execSQL(query);
    }
}
