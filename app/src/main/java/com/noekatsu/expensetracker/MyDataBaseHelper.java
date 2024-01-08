package com.noekatsu.expensetracker;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyDataBaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "ExpenseTracker.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "Today_Expenses";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TYPE = "Type";
    private static final String COLUMN_AMOUNT = "Amount";
    private static final String COLUMN_DATE = "Date";


    public MyDataBaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context ;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TYPE + " TEXT, " +
                        COLUMN_AMOUNT + " INTEGER, " +
                        COLUMN_DATE + " TEXT);";

        db.execSQL(query);
    }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                if (newVersion > oldVersion) {
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                    onCreate(db);
                }
            } catch (Exception e) {
                Log.e("MyDataBaseHelper", "Error upgrading database", e);
            }
        }


    void addExpense(String type , int amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TYPE,type);
        cv.put(COLUMN_AMOUNT,amount);
        cv.put(COLUMN_DATE, getCurrentDate());
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context , "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context , "Added Successfully!", Toast.LENGTH_SHORT).show();

        }
    }

    void updateData(String row_id, String type, String amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_AMOUNT,amount);

        long result = db.update(TABLE_NAME,cv,"_id=?",new String[]{row_id});
        if (result == -1){
            Toast.makeText(context,"Failed to Update", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Successfully Updated!", Toast.LENGTH_SHORT).show();

        }
    }

    void deleteOneRow(String row_id){
    SQLiteDatabase db = this.getWritableDatabase();
    long result = db.delete(TABLE_NAME,"_id=?",new String[]{row_id});
    if(result == -1){
        Toast.makeText(context,"Failed To Delete", Toast.LENGTH_SHORT).show();
    }else{
        Toast.makeText(context,"Successfully Deleted", Toast.LENGTH_SHORT).show();
    }
    }

    void deleteAllExpenses(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
    public List<ExpenseModel> getExpensesForDate(String selectedDate) {
        List<ExpenseModel> expensesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {COLUMN_TYPE, COLUMN_AMOUNT, COLUMN_DATE};

        String selection = COLUMN_DATE + " = ?";

        String[] selectionArgs = {selectedDate};

        Cursor cursor = db.query(
                TABLE_NAME,          // The table to query
                projection,          // The columns to return
                selection,           // The columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
                null,                // Don't group the rows
                null,                // Don't filter by row groups
                null                 // The sort order
        );

        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));
            int amount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));

            ExpenseModel expense = new ExpenseModel(type, amount, date);
            expensesList.add(expense);
        }

        cursor.close();

        return expensesList;
    }
    public double getTotalExpensesForDate(String selectedDate) {
        double totalAmount = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {COLUMN_AMOUNT};
        String selection = COLUMN_DATE + " = ?";
        String[] selectionArgs = {selectedDate};

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            totalAmount += cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
        }

        cursor.close();
        return totalAmount;
    }

    public Cursor readAllDataForDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = ?";
        return db.rawQuery(query, new String[]{date});
    }

    public int deleteExpensesForDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_DATE + "=?", new String[]{date});
    }

}


