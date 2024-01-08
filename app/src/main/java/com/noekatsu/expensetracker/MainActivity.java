package com.noekatsu.expensetracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    ImageView empty_img;
    TextView no_expenses;


    FloatingActionButton add_button;
    MyDataBaseHelper myDB;
    ArrayList<String> expense_id , expense_type,expense_amount;
    double totalAmount = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnToday = findViewById(R.id.btnToday);
        Button btnWeek = findViewById(R.id.btnWeek);
        ImageButton btnDelete = findViewById(R.id.btnDelete);
        empty_img = findViewById(R.id.empty_img);
        no_expenses = findViewById(R.id.no_expenses);
        recyclerView = findViewById(R.id.recycler);
        add_button = findViewById(R.id.floatingBtn);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
        myDB = new MyDataBaseHelper(MainActivity.this);
        expense_id = new ArrayList<>();
        expense_type = new ArrayList<>();
        expense_amount = new ArrayList<>();

        storeDataInArray();
        customAdapter = new CustomAdapter(MainActivity.this,this,expense_id,
                expense_type,expense_amount);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
;
            }
        });

        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weeklyIntent = new Intent(MainActivity.this, WeeklyActivity.class);
                startActivity(weeklyIntent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
            customAdapter.notifyDataSetChanged();
        }
    }


    void storeDataInArray() {
        expense_id.clear();
        expense_type.clear();
        expense_amount.clear();
        totalAmount = 0.0;
        ImageView emptyImageView = findViewById(R.id.empty_img);
        TextView noExpensesTextView = findViewById(R.id.no_expenses);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());

        Cursor cursor = myDB.readAllDataForDate(currentDate);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Expenses Available for Today..", Toast.LENGTH_SHORT).show();
            emptyImageView.setVisibility(View.VISIBLE);
            noExpensesTextView.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                expense_id.add(cursor.getString(0));
                expense_type.add(cursor.getString(1));
                String amount = cursor.getString(2);
                expense_amount.add(amount);
                totalAmount += Double.parseDouble(amount);

            }
            emptyImageView.setVisibility(View.GONE);
            noExpensesTextView.setVisibility(View.GONE);
        }
        updateTotalUI(totalAmount);
    }

    private void updateTotalUI(double totalAmount) {
        TextView totalTextView = findViewById(R.id.totalTextView);
        totalTextView.setText("Total: $" + totalAmount);
    }
    @Override
    protected void onResume() {
        super.onResume();
        storeDataInArray();
        customAdapter.notifyDataSetChanged();
    }



    void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Today's Expenses?");
        builder.setMessage("Are you sure you want to delete all expenses for today?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDataBaseHelper myDB = new MyDataBaseHelper(MainActivity.this);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String currentDate = sdf.format(calendar.getTime());

                int rowsDeleted = myDB.deleteExpensesForDate(currentDate);

                if (rowsDeleted > 0) {
                    Toast.makeText(MainActivity.this, "Deleted successfully...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No expenses for today", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing if "No" is clicked
            }
        });
        builder.create().show();
    }





}