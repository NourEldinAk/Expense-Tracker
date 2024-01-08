package com.noekatsu.expensetracker;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    EditText type_input , amount_input;
    Button update_button;
    Button detele_button;
    String id,type,amount;
    Button go_back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);
        type_input = findViewById(R.id.expense_type2);
        amount_input=  findViewById(R.id.expense_amount2);
        update_button = findViewById(R.id.update_button);
        detele_button = findViewById(R.id.delete_button);
        getAndSetIntentData();

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDataBaseHelper myDB = new MyDataBaseHelper(UpdateActivity.this);
                myDB.updateData(id,type_input.getText().toString(),amount_input.getText().toString());
                finish();

            }
        });

        detele_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });

        go_back_button = findViewById(R.id.go_back_button);
        go_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


    }

   void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("type")&& getIntent().hasExtra("amount")){
            id = getIntent().getStringExtra("id");
            type = getIntent().getStringExtra("type");
            amount = getIntent().getStringExtra("amount");
            type_input.setText(type);
            amount_input.setText(amount);
        }else{
            Toast.makeText(this,"No data ..", Toast.LENGTH_SHORT).show();
        }
    }
    void confirmDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + type + " ?");
        builder.setMessage("Are you sure you want to delete "+ type + " expense ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDataBaseHelper myDB = new MyDataBaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
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