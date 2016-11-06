package net.dauhuthom.greennote;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddServiceActivity extends AppCompatActivity {

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;

    EditText etServiceName;
    Button btnSaveService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        addControls();
        addEvents();
    }

    private void addEvents() {
        //insert
        btnSaveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });
    }

    private void addControls() {
        etServiceName = (EditText) findViewById(R.id.etServiceName);
        btnSaveService = (Button) findViewById(R.id.btnSaveService);
    }

    private void insert() {
        String name = etServiceName.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);

        database = Database.initDatabase(this, DATABASE_NAME);
        database.insert("services", null, contentValues);
        Intent intent = new Intent(this, ServiceActivity.class);
        startActivity(intent);
    }
}
