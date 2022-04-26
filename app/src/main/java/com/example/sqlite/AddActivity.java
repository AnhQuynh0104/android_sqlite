package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner;
    private EditText editTitle, editPrice, editDate;
    private Button btnUpdate, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
    }

    private void initView() {
        spinner = findViewById(R.id.spinnerCategory);
        editTitle = findViewById(R.id.editTitle);
        editPrice = findViewById(R.id.editPrice);
        editDate = findViewById(R.id.editDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner, getResources().getStringArray(R.array.category)));
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        editDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == editDate){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                Item item = new Item( title, category, price, date);
                SQLiteHelper db = new SQLiteHelper(this);
                db.addItem(item);
                finish();
            }
        }
    }
}