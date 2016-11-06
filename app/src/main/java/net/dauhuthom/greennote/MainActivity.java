package net.dauhuthom.greennote;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    final String DATABASE_NAME = "GreenNote.sqlite";
    SQLiteDatabase database;

    Button btnToService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM services", null);
        cursor.moveToFirst();
        Toast.makeText(this, cursor.getString(1), Toast.LENGTH_LONG).show();

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnToService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        btnToService = (Button) findViewById(R.id.btnToService);
    }


}
