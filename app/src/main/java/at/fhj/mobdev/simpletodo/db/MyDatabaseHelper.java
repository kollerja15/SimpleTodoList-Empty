package at.fhj.mobdev.simpletodo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mynewdb.db";
    public static final int DATABASE_VERSION = 1;


    public static final String TO_DO_LIST_TABLE = "mytable";
    public static final String TO_DO_LIST_TABLE_COLUMN_DESCRIPTION = "description";
    public static final String TO_DO_LIST_TABLE_COLUMN_TITLE = "title";
    public static final String TO_DO_LIST_TABLE_COLUMN_ID ="id";


    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TO_DO_LIST_TABLE + " (" +
                TO_DO_LIST_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                TO_DO_LIST_TABLE_COLUMN_DESCRIPTION + " TEXT, " +
                TO_DO_LIST_TABLE_COLUMN_TITLE + " VARCHAR(100)" +")");

        db.execSQL("Insert into mytable (title, description) values (\"test\", \"dsfdf\")");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



    }

}