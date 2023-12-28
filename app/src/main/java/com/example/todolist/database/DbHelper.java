package com.example.todolist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(Context context){
        super(context, "ToDoDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE TODO(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "TITLE TEXT, CONTENT TEXT, DATE TEXT)";
        sqLiteDatabase.execSQL(sql);
        String data = "INSERT INTO TODO VALUES(1, 'Học Java', 'Học Java Cơ bản', '21/6/2023')," +
                "(2, 'Học HTML', 'Học HTML Cơ bản', '22/9/2023')," +
                "(3, 'Học CSS', 'Học CSS Nâng cao', '30/8/2023')";
        sqLiteDatabase.execSQL(data);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int olderVersion, int newVersion) {
        if(olderVersion != newVersion){
            sqLiteDatabase.execSQL("DROP TABLE TODO");
            onCreate(sqLiteDatabase);
        }

    }
}
