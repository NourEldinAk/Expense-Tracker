package com.noekatsu.expensetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList expense_id , expense_type, expense_amount;
    Activity activity;
    CustomAdapter(Activity activity, Context context,
                  ArrayList expense_id,
                  ArrayList expense_type,
                  ArrayList expense_amount){
        this.context = context;
        this.expense_id = expense_id;
        this.expense_type = expense_type;
        this.expense_amount = expense_amount;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.expense_row,parent,false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder.expense_id.setText(String.valueOf(expense_id.get(position)));
        holder.expense_type.setText(String.valueOf(expense_type.get(position)));
        holder.expense_amount.setText("$"+String.valueOf(expense_amount.get(position)));


            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();

                        Intent intent = new Intent(context, UpdateActivity.class);
                        intent.putExtra("id", String.valueOf(expense_id.get(adapterPosition)));
                        intent.putExtra("type", String.valueOf(expense_type.get(adapterPosition)));
                        intent.putExtra("amount", String.valueOf(expense_amount.get(adapterPosition)));
                        activity.startActivityForResult(intent, 1);

                }
            });

    }


    @Override
    public int getItemCount() {
        return expense_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView expense_id , expense_type,expense_amount;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            expense_id = itemView.findViewById(R.id.card_id);
            expense_type = itemView.findViewById(R.id.card_type);
            expense_amount = itemView.findViewById(R.id.card_amount);
            mainLayout = itemView.findViewById(R.id.layoutMain);
        }
    }
}
