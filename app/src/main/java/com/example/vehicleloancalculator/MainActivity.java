package com.example.vehicleloancalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextVehiclePrice, editTextDownPayment, editTextLoanPeriod, editTextInterestRate;
    private CardView cardViewResults;
    private TextView textViewLoanAmount, textViewTotalInterest, textViewTotalPayment, textViewMonthlyPayment;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editTextVehiclePrice = findViewById(R.id.editTextVehiclePrice);
        editTextDownPayment = findViewById(R.id.editTextDownPayment);
        editTextLoanPeriod = findViewById(R.id.editTextLoanPeriod);
        editTextInterestRate = findViewById(R.id.editTextInterestRate);

        Button buttonCalculate = findViewById(R.id.buttonCalculate);
        Button buttonReset = findViewById(R.id.buttonReset);

        cardViewResults = findViewById(R.id.cardViewResults);
        textViewLoanAmount = findViewById(R.id.textViewLoanAmount);
        textViewTotalInterest = findViewById(R.id.textViewTotalInterest);
        textViewTotalPayment = findViewById(R.id.textViewTotalPayment);
        textViewMonthlyPayment = findViewById(R.id.textViewMonthlyPayment);


        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateLoan();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void calculateLoan() {
        // Get input values
        String priceStr = editTextVehiclePrice.getText().toString();
        String downStr = editTextDownPayment.getText().toString();
        String periodStr = editTextLoanPeriod.getText().toString();
        String rateStr = editTextInterestRate.getText().toString();

        // Validate inputs
        if (priceStr.isEmpty() || downStr.isEmpty() || periodStr.isEmpty() || rateStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double vehiclePrice = Double.parseDouble(priceStr);
            double downPayment = Double.parseDouble(downStr);
            double loanPeriod = Double.parseDouble(periodStr);
            double interestRate = Double.parseDouble(rateStr);

            // Validate values
            if (vehiclePrice <= 0 || loanPeriod <= 0 || interestRate < 0) {
                Toast.makeText(this, "Please enter valid positive values", Toast.LENGTH_SHORT).show();
                return;
            }

            if (downPayment >= vehiclePrice) {
                Toast.makeText(this, "Down payment must be less than vehicle price", Toast.LENGTH_SHORT).show();
                return;
            }

            //  Calculate Loan Amount
            double loanAmount = vehiclePrice - downPayment;

            //  Calculate Total Interest
            double totalInterest = loanAmount * (interestRate / 100) * loanPeriod;

            //  Calculate Total Payment
            double totalPayment = loanAmount + totalInterest;

            //  Calculate Monthly Payment
            double monthlyPayment = totalPayment / (loanPeriod * 12);

            // Display results
            textViewLoanAmount.setText("RM " + df.format(loanAmount));
            textViewTotalInterest.setText("RM " + df.format(totalInterest));
            textViewTotalPayment.setText("RM " + df.format(totalPayment));
            textViewMonthlyPayment.setText("RM " + df.format(monthlyPayment));

            // Show results card
            cardViewResults.setVisibility(View.VISIBLE);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetForm() {
        editTextVehiclePrice.setText("");
        editTextDownPayment.setText("");
        editTextLoanPeriod.setText("");
        editTextInterestRate.setText("");
        cardViewResults.setVisibility(View.GONE);
        Toast.makeText(this, "Form reset", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            Toast.makeText(this, "Already on Home", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}