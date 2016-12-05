package net.dauhuthom.greennote;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditServiceActivity extends AppCompatActivity {

    ServiceDBHelper serviceDBHelper;
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
        serviceDBHelper = new ServiceDBHelper(getApplicationContext());
        Cursor cursor = serviceDBHelper.get(id);
        cursor.moveToFirst();
        String name = cursor.getString(1);
        etServiceName.setText(name);
    }

    private void addEvents() {
        //update
        btnSaveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    String name = etServiceName.getText().toString();
                    serviceDBHelper = new ServiceDBHelper(getApplicationContext());
                    serviceDBHelper.update(id, name);

                    Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void addControls() {
        etServiceName = (EditText) findViewById(R.id.etServiceName);
        btnSaveService = (Button) findViewById(R.id.btnSaveService);
    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(etServiceName.getText().toString().trim())) {
            etServiceName.setError("Vui lòng nhập tên danh mục chi tiêu!");
            return false;
        }

        return true;
    }
}
