package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sqlite.dal.SQLiteHelper;
import com.example.sqlite.model.Item;

import java.util.Calendar;

public class UpdateDeleteActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner;
    private EditText editTitle, editPrice, editDate;
    private Button btnUpdate, btnDelete, btnCancel;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        initView();
    }

    private void initView() {
        spinner = findViewById(R.id.spinnerCategory);
        editTitle = findViewById(R.id.editTitle);
        editPrice = findViewById(R.id.editPrice);
        editDate = findViewById(R.id.editDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnCancel = findViewById(R.id.btnCancel);
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner, getResources().getStringArray(R.array.category)));
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        editDate.setOnClickListener(this);
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        editTitle.setText(item.getTitle());
        editPrice.setText(item.getPrice());
        editDate.setText(item.getDate());
        int p = 0;
        for(int i = 0; i < spinner.getCount();i++){
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item.getCategory())){
                p = i;
                break;
            }
        }
        spinner.setSelection(p);
    }

    @Override
    public void onClick(View v) {
        SQLiteHelper db = new SQLiteHelper(this);
        if(v == editDate){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(UpdateDeleteActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = "";
                    if(month > 8){
                        date = day + "/" + (month + 1) + "/" + year;
                    } else {
                        date = day + "/0" + (month + 1) + "/" + year;
                    }
                    editDate.setText(date);
                }
            }, year, month, day);
            dialog.show();
        }
        if(v == btnCancel){
            finish();
        }
        if(v == btnUpdate){
            String title = editTitle.getText().toString();
            String price = editPrice.getText().toString();
            String date = editDate.getText().toString();
            String category = spinner.getSelectedItem().toString();
            if(!title.isEmpty() && price.matches("\\d+")){
                int id = item.getId();
                Item item = new Item(id, title, category, price, date);
                db.update(item);
            }
            finish();
        }

        if(v == btnDelete){
            int id = item.getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Thong bao xoa");
            builder.setMessage("Ban co muon xoa?");
            builder.setPositiveButton("Co", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.delete(id);
                    finish();
                }
            });
            builder.setNegativeButton("Khong", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}