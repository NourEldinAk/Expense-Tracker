package com.noekatsu.expensetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.WeekViewHolder> {

    private List<String> weekList;
    private List<Double> dailyTotals;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String day);
    }

    public WeeklyAdapter(List<String> weekList, List<Double> dailyTotals, OnItemClickListener onItemClickListener) {
        this.weekList = weekList;
        this.dailyTotals = dailyTotals;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_item, parent, false);
        return new WeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {
        String day = weekList.get(position);
        double total = dailyTotals.get(position);

        holder.dayTextView.setText(day);
        holder.totalTextView.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(day);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weekList.size();
    }

    public class WeekViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;
        public TextView totalTextView;

        public WeekViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            totalTextView = itemView.findViewById(R.id.totalTextView);
        }
    }
}
