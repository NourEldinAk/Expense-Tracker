package com.noekatsu.expensetracker;

        import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    EditText type_input , amount_input;
    Button add_button,go_back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);


        type_input = findViewById(R.id.expense_type);
        amount_input = findViewById(R.id.expense_amount);
        add_button = findViewById(R.id.add_button);
        go_back_button = findViewById(R.id.go_back_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = type_input.getText().toString().trim();
                String amountString = amount_input.getText().toString().trim();

                if (type.isEmpty() || amountString.isEmpty()) {
                    Toast.makeText(AddActivity.this, "Fields shouldn't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int amount = Integer.parseInt(amountString);

                    MyDataBaseHelper myDB = new MyDataBaseHelper(AddActivity.this);
                    myDB.addExpense(type, amount);

                    Toast.makeText(AddActivity.this, "Expense added successfully", Toast.LENGTH_SHORT).show();

                    finish();
                } catch (NumberFormatException e) {

                    Toast.makeText(AddActivity.this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        go_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }



}