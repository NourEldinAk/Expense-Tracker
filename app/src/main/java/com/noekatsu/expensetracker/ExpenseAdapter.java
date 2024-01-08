package com.noekatsu.expensetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// ExpenseAdapter.java
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<ExpenseModel> expensesList;

    public ExpenseAdapter(List<ExpenseModel> expensesList) {
        this.expensesList = expensesList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseModel expense = expensesList.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView expenseTypeTextView;
        private TextView expenseAmountTextView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseTypeTextView = itemView.findViewById(R.id.expenseTypeTextView);
            expenseAmountTextView = itemView.findViewById(R.id.expenseAmountTextView);
        }

        public void bind(ExpenseModel expense) {
            expenseTypeTextView.setText(expense.getType());
            expenseAmountTextView.setText("$"+String.valueOf(expense.getAmount()));
        }
    }
}

