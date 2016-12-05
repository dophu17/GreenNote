package net.dauhuthom.greennote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by phu on 11/7/2016.
 */

public class AdapterNote extends BaseAdapter {

    Activity context;
    ArrayList<Note> list;
    String currentDate;

    public AdapterNote(Activity context, ArrayList<Note> list, String currentDate) {
        this.context = context;
        this.list = list;
        this.currentDate = currentDate;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
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
        String des = "";
        String str = note.description;
        if (str.length() > 0) {
            des = " (" + str + ")";
        }
        tvName.setText(note.service_name + des);
        tvPrice.setText(new Function().formatDecimal(note.price, "###,###,###,###,###", Locale.GERMANY) + " VND");

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditNoteActivity.class);
                intent.putExtra("id", note.id);
                context.startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa chi tiêu này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NoteDBHelper noteDBHelper = new NoteDBHelper(context);
                        noteDBHelper.delete(note.id);

                        noteDBHelper = new NoteDBHelper(context);
                        Cursor cursor = noteDBHelper.getAllJoinByDate(currentDate);
                        list.clear();
                        while (cursor.moveToNext()) {
                            int id = cursor.getInt(cursor.getColumnIndex("id"));
                            int service_id = cursor.getInt(cursor.getColumnIndex("service_id"));
                            double price = cursor.getDouble(cursor.getColumnIndex("price"));
                            String date = cursor.getString(cursor.getColumnIndex("date"));
                            String description = cursor.getString(cursor.getColumnIndex("description"));
                            String service_name = cursor.getString(cursor.getColumnIndex("name"));
                            list.add(new Note(id, service_id, price, date, description, service_name));
                        }
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });

        return row;
    }
}
