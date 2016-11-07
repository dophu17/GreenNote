package net.dauhuthom.greennote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by phu on 11/7/2016.
 */

public class AdapterNote extends BaseAdapter {

    Activity context;
    ArrayList<Note> list;
    final String DATABASE_NAME = "GreenNote.sqlite";

    public AdapterNote(Activity context, ArrayList<Note> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.lv_row_note, null);
        TextView tvName = (TextView) row.findViewById(R.id.tvName);
        TextView tvPrice = (TextView) row.findViewById(R.id.tvPrice);
        Button btnEdit = (Button) row.findViewById(R.id.btnEdit);
        Button btnDelete = (Button) row.findViewById(R.id.btnDelete);

        final Note note = list.get(i);
        tvName.setText(note.service_id);
        tvPrice.setText(note.price + "VND");

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditServiceActivity.class);
                intent.putExtra("id", note.id);
                context.startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                delete(note.id);
            }
        });

        return row;
    }

//    private void delete(int idNote) {
//        SQLiteDatabase database = Database.initDatabase(context, DATABASE_NAME);
//        database.delete("notes", "id = ?", new String[]{idNote + ""});
//        Cursor cursor = database.rawQuery("SELECT * FROM notes", null);
//        list.clear();
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            int service_id = cursor.getInt(1);
//            double price = cursor.getDouble(2);
//            String date = cursor.getString(3);
//            String description = cursor.getString(4);
//            list.add(new Note(id, service_id, price, date, description));
//        }
//        notifyDataSetChanged();
//    }
}
