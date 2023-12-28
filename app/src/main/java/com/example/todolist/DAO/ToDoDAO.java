package com.example.todolist.DAO;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.todolist.Model.ToDoModel;
import com.example.todolist.database.DbHelper;

import java.util.ArrayList;

public class ToDoDAO {
    private final DbHelper dbHelper;

    public ToDoDAO(Context context){
        this.dbHelper = new DbHelper(context);
    }

    public ArrayList<ToDoModel> getListToDo(){
        ArrayList<ToDoModel> list = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        database.beginTransaction();
        try{
            Cursor cursor = database.rawQuery("SELECT * FROM TODO", null);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                do{
                    list.add(new ToDoModel(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3)));
                }while (cursor.moveToNext());
                database.setTransactionSuccessful();
            }
        }catch (Exception e){
            Log.e(TAG, "getListTodo", e);
        }finally {
            database.endTransaction();
        }
        return list;
    }

    public boolean AddListToDo(ToDoModel toDoModel){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITLE", toDoModel.getTitle());
        contentValues.put("CONTENT", toDoModel.getContent());
        contentValues.put("DATE", toDoModel.getDate());
        long check = sqLiteDatabase.insert("TODO", null, contentValues);
        return check != -1;

    }

    public boolean UpdateListToDo(ToDoModel toDoModel){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITLE", toDoModel.getTitle());
        contentValues.put("CONTENT", toDoModel.getContent());
        contentValues.put("DATE", toDoModel.getDate());
        long check = sqLiteDatabase.update("TODO", contentValues, "ID=?", new String[]{String.valueOf(toDoModel.getId())});
        return check != -1;
    }

    public boolean DeleteListToDo(int id){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        long check = sqLiteDatabase.delete("TODO", "ID=?", new String[]{String.valueOf(id)});
        return check != -1;
    }
}
