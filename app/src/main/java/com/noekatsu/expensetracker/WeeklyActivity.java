package com.noekatsu.expensetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeeklyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_activity);
        Button btnToday = findViewById(R.id.btnToday);
        Button btnWeek = findViewById(R.id.btnWeek);
        ImageButton btnDelete = findViewById(R.id.btnDelete);

        // Fetch the list of days for the current week
        List<String> daysList = getDaysForCurrentWeek();

        MyDataBaseHelper myDB = new MyDataBaseHelper(this);
        List<Double> dailyTotals = new ArrayList<>();
        for (String day : daysList) {
            double total = myDB.getTotalExpensesForDate(day);
            dailyTotals.add(total);
        }

        RecyclerView weekRecyclerView = findViewById(R.id.weekRecyclerView);
        WeeklyAdapter weekAdapter = new WeeklyAdapter(daysList, dailyTotals, new WeeklyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String day) {
                Intent intent = new Intent(WeeklyActivity.this, DailyExpensesActivity.class);
                intent.putExtra("selectedDay", day);
                startActivity(intent);
            }
        });

        weekRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weekRecyclerView.setAdapter(weekAdapter);
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            confirmDelete();
            }
        });
    }


    private List<String> getDaysForCurrentWeek() {
        List<String> daysList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        // Fetch days for the entire week
        for (int i = 0; i < 7; i++) {
            daysList.add(sdf.format(calendar.getTime()));
            // Move to the next day
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        return daysList;
    }
    void confirmDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All ?");
        builder.setMessage("Are you sure you want to delete All Expenses ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDataBaseHelper myDB = new MyDataBaseHelper(WeeklyActivity.this);
                myDB.deleteAllExpenses();
                Toast.makeText(WeeklyActivity.this, "Delete..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WeeklyActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
