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

public class EditServiceActivity extends AppCompatActivity {

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;
    int id = -1;

    EditText etServiceName;
    Button btnSaveService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        addControls();
        initUI();
        addEvents();
    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM services WHERE id = ?", new String[]{id + ""});
        cursor.moveToFirst();
        String name = cursor.getString(1);
        etServiceName.setText(name);
    }

    private void addEvents() {
        //insert
        btnSaveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    private void addControls() {
        etServiceName = (EditText) findViewById(R.id.etServiceName);
        btnSaveService = (Button) findViewById(R.id.btnSaveService);
    }

    private void update() {
        String name = etServiceName.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);

        database = Database.initDatabase(this, DATABASE_NAME);
        database.update("services", contentValues, "id = ?", new String[]{id + ""});
        Intent intent = new Intent(this, ServiceActivity.class);
        startActivity(intent);
    }
}
