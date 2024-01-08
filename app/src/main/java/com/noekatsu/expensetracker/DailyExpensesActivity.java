package com.noekatsu.expensetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DailyExpensesActivity extends AppCompatActivity {
    private RecyclerView expensesRecyclerView;
    private ExpenseAdapter expenseAdapter;
    private TextView noExpensesTextView;
    private View emptyImageView;
    Button go_back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_expenses);

        String selectedDate = getIntent().getStringExtra("selectedDay");

        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("Expenses - " + selectedDate);

        expensesRecyclerView = findViewById(R.id.expensesRecyclerView);
        expensesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        noExpensesTextView = findViewById(R.id.no_expenses);
        emptyImageView = findViewById(R.id.empty_img);

        List<ExpenseModel> expensesList = fetchExpensesForDate(selectedDate);

        if (expensesList.isEmpty()) {
            emptyImageView.setVisibility(View.VISIBLE);
            noExpensesTextView.setVisibility(View.VISIBLE);
        } else {
            emptyImageView.setVisibility(View.GONE);
            noExpensesTextView.setVisibility(View.GONE);
        }

        expenseAdapter = new ExpenseAdapter(expensesList);
        expensesRecyclerView.setAdapter(expenseAdapter);

        go_back_button = findViewById(R.id.go_back_button);
        go_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private List<ExpenseModel> fetchExpensesForDate(String selectedDate) {
        MyDataBaseHelper myDB = new MyDataBaseHelper(this);
        return myDB.getExpensesForDate(selectedDate);
    }



}
