package at.fhj.mobdev.simpletodo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import at.fhj.mobdev.simpletodo.db.MyDatabaseHelper;

@SuppressLint("StaticFieldLeak")
public class CreateOrEditActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;

    private static final String TAG = "CreateOrEditActivity";

    public static final String EXTRA_TODO_ID = "id";

    private long todoId;

    private boolean createMode;

    private TextView title;

    private TextView description;

    private CheckBox done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todoId = getIntent().getLongExtra(EXTRA_TODO_ID, 0);
        createMode = todoId == 0;
        Log.i(TAG, "onCreate, given todoId is " + todoId);

        setContentView(R.layout.activity_create_or_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button delete = findViewById(R.id.delete);
        delete.setVisibility(createMode ? View.GONE : View.VISIBLE);
        delete.setOnClickListener(this);

        Button save = findViewById(R.id.save);
        save.setOnClickListener(this);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        done = findViewById(R.id.done);

        setTitle(createMode ? "Create item" : "Edit item");
    }

    private void updateResultAndFinish(boolean deleted) {
        Intent data = new Intent();
        // used to inform the main activity to that it can show a notification to the user
        data.putExtra(MainActivity.EXTRA_CHANGED_TITLE, title.getText().toString());
        data.putExtra(MainActivity.EXTRA_DELETED, deleted);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!createMode) {
            loadData();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save) {
            saveData();
            updateResultAndFinish(false);
        } else if (v.getId() == R.id.delete) {
            deleteData();
            updateResultAndFinish(true);
        }
    }

    private void loadData() {

        SQLiteDatabase db = new MyDatabaseHelper(this).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM mytable WHERE id = " + todoId, null);
        String d2 = "";
        String d3 = "";
        while (cursor.moveToNext()) {
            // alternative to above (ask sqlite for the correct index)
            d2 = cursor.getString(cursor.getColumnIndex("title"));
            d3 = cursor.getString(cursor.getColumnIndex("description"));
        }
        // always close cursors to prevent memory leaks
        title.setText(d2);
        description.setText(d3);

        cursor.close();

    }

    private void saveData() {

        SQLiteDatabase database = new MyDatabaseHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MyDatabaseHelper.TO_DO_LIST_TABLE_COLUMN_TITLE,title.getText().toString());
        values.put(MyDatabaseHelper.TO_DO_LIST_TABLE_COLUMN_DESCRIPTION,description.getText().toString());
        if(todoId==0){
            long newRowId = database.insert(MyDatabaseHelper.TO_DO_LIST_TABLE, null, values);
        }else{
            database.update("mytable", values, "rowid = ?", new String[] {Long.toString(todoId )});
        }

    }

    private void deleteData() {

        SQLiteDatabase database = new MyDatabaseHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();

        database.delete("mytable", "id=?", new String[]{new Long(todoId).toString()});
    }
}