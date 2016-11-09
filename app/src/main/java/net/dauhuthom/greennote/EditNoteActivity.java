package net.dauhuthom.greennote;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNoteActivity extends AppCompatActivity {

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;
    int id = -1;
    int ServiceID = -1;

    EditText etPrice, etDescription, etDate;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        addControls();
        initUI();
        addEvents();
    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM notes WHERE id = ?", new String[]{id + ""});
        cursor.moveToFirst();
        String price = cursor.getString(cursor.getColumnIndex("price"));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        String date = cursor.getString(cursor.getColumnIndex("date"));
        etPrice.setText(price);
        etDescription.setText(description);
        etDate.setText(date);
    }

    private void addEvents() {
        //insert
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    private void addControls() {
        etPrice = (EditText) findViewById(R.id.etCost);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etDate = (EditText) findViewById(R.id.etDate);
        btnSave = (Button) findViewById(R.id.btnSave);
    }

    private void update() {
        String price = etPrice.getText().toString();
        String description = etDescription.getText().toString();
        String date = etDate.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("price", price);
        contentValues.put("description", description);
        contentValues.put("date", date);

        database = Database.initDatabase(this, DATABASE_NAME);
        database.update("notes", contentValues, "id = ?", new String[]{id + ""});
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
