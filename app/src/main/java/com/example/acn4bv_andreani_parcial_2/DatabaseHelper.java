package com.example.acn4bv_andreani_parcial_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "DaVinci_Parcial2";
    private static final int DATABASE_VERSION = 3;

    public static final String TASKS_TABLE   = "tasks";
    public static final String T_COL_ID      = "id";
    public static final String T_COL_TITLE   = "title";
    public static final String T_COL_DESCRIPTION = "description";
    public static final String T_COL_TIME    = "time";
    public static final String T_COL_DATE = "date";
    public static final String T_COL_DONE    = "done";

    private static final String CREATE_TABLE_TASKS =
            "CREATE TABLE " + TASKS_TABLE + " (" +
                    T_COL_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    T_COL_TITLE     + " TEXT NOT NULL, " +
                    T_COL_DESCRIPTION + " TEXT, " +
                    T_COL_TIME      + " TEXT, " +
                    T_COL_DATE      + " TEXT," +
                    T_COL_DONE      + " INTEGER NOT NULL DEFAULT 0" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Executing CREATE TABLE: " + CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ". Dropping old table.");
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE);
        onCreate(db);
    }

    public boolean insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T_COL_TITLE, task.getTitle());
        cv.put(T_COL_DESCRIPTION, task.getDescription());
        cv.put(T_COL_TIME, task.getTime());
        cv.put(T_COL_DATE, task.getDate());
        cv.put(T_COL_DONE, task.isDone());
        long res = db.insert(TASKS_TABLE, null, cv);
        db.close();

        if (res == -1) {
            Log.e(TAG, "ERROR: No se pudo insertar la tarea en la base de datos. Título: " + task.getTitle() + " Fecha: " + task.getDate());
            return false;
        } else {
            Log.d(TAG, "SUCCESS: Tarea insertada con ID: " + res + " Título: " + task.getTitle() + " Fecha: " + task.getDate());
            return true;
        }
    }

    public List<Task> getTasksForDate(String dateString) {
        List<Task> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TASKS_TABLE + " WHERE " + T_COL_DATE + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, new String[]{dateString});

        if (cursor.moveToFirst()) {
            do {
                int taskID = cursor.getInt(cursor.getColumnIndexOrThrow(T_COL_ID));
                String taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(T_COL_TITLE));
                String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(T_COL_DESCRIPTION));
                String taskTime = cursor.getString(cursor.getColumnIndexOrThrow(T_COL_TIME));
                String taskDate = cursor.getString(cursor.getColumnIndexOrThrow(T_COL_DATE));
                int isDone = cursor.getInt(cursor.getColumnIndexOrThrow(T_COL_DONE));

                Task newTask = new Task(taskID, taskTitle, taskDescription, taskTime, taskDate, isDone);
                returnList.add(newTask);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d(TAG, "getTasksForDate: Se encontraron " + returnList.size() + " tareas para la fecha " + dateString);
        return returnList;
    }

    public boolean deleteTask(int taskId){
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRows = db.delete(TASKS_TABLE, T_COL_ID + " = ?",
                new String[]{String.valueOf(taskId)});
        db.close();
        if (affectedRows > 0) {
            Log.d(TAG, "SUCCESS: Tarea con ID " + taskId + " eliminada.");
            return true;
        } else {
            Log.e(TAG, "ERROR: No se pudo eliminar la tarea con ID " + taskId + ".");
            return false;
        }
    }

    public boolean updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T_COL_TITLE, task.getTitle());
        cv.put(T_COL_DESCRIPTION, task.getDescription());
        cv.put(T_COL_TIME, task.getTime());
        cv.put(T_COL_DATE, task.getDate());
        cv.put(T_COL_DONE, task.isDone());

        int affectedRows = db.update(TASKS_TABLE, cv, T_COL_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
        if (affectedRows > 0) {
            Log.d(TAG, "SUCCESS: Tarea con ID " + task.getId() + " actualizada. Título: " + task.getTitle());
            return true;
        } else {
            Log.e(TAG, "ERROR: No se pudo actualizar la tarea con ID " + task.getId() + ".");
            return false;
        }
    }

    //metodos para el perfil
    public int getAllTasksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TASKS_TABLE, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public String getFirstTaskDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String firstDate = null;
        Cursor cursor = db.rawQuery("SELECT MIN(" + T_COL_DATE + ") FROM " + TASKS_TABLE, null);
        if (cursor.moveToFirst()) {
            firstDate = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return firstDate;
    }
}