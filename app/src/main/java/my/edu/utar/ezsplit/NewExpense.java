package my.edu.utar.ezsplit;

import android.app.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewExpense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editPurpose;
    private EditText editPayer;

    private int breakDownMethod = 0;

    private EditText editTotalAmount;
    private String strTotalAmount;
    public double totalAmount;

    private CardView cvPickDate;
    private TextView tvSelectedDate;

    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;

    private RecyclerView rvOwe;
    private DataRecyclerViewAdapter adapter;
    private SQLiteAdapter mySQLiteAdapter;

    private static final int REQUEST_CODE = 1;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        getSupportActionBar().setTitle(R.string.tx_new_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable the back button in the app bar


        // Retrieve data from database
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        List<Ower> memberNameList = mySQLiteAdapter.queueAllMember();
        DataRecyclerViewAdapter.dataList = memberNameList;

        mySQLiteAdapter.close();


        editPurpose = findViewById(R.id.editPurpose);
        editPayer = findViewById(R.id.editPayer);


        // To hide the keyboard after user enters the total amount
        editTotalAmount = findViewById(R.id.editTotalAmount);

        editTotalAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Check if the "Done" or "Enter" key was pressed
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        cvPickDate = findViewById(R.id.cvDate);
        tvSelectedDate = findViewById(R.id.txSelectedDate);

        // Use the current date as the default date in the date picker
        final Calendar c = Calendar.getInstance();
        selectedYear = c.get(Calendar.YEAR);
        selectedMonth = c.get(Calendar.MONTH);
        selectedDay = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = sdf.format(c.getTime());
        tvSelectedDate.setText(dateString);

        // To add a listener for pick date
        cvPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = selectedYear;
                int month = selectedMonth;
                int day = selectedDay;

                // Create a variable for date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewExpense.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        String dateString = sdf.format(c.getTime());
                        tvSelectedDate.setText(dateString);

                        selectedYear = year;
                        selectedMonth = monthOfYear;
                        selectedDay = dayOfMonth;
                    }
                },
                        // Pass year, month and day for selected date in date picker
                        year, month, day);
                // Display date picker dialog
                datePickerDialog.show();
            }
        });


        Spinner dropdown = findViewById(R.id.spinner);
        dropdown.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        // Create a list of items for the spinner
        String[] breakDownMethods = getResources().getStringArray(R.array.break_down_methods);

        // Create a custom ArrayAdapter to disable the default item
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, breakDownMethods) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Disable the first item (default value)
            }
        };

        dropdown.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_expense, menu);
        return true;
    }

    // Handle the tick click event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_check) {
            String strPurpose = editPurpose.getText().toString().trim();
            String strPayer = editPayer.getText().toString().trim();
            strTotalAmount = editTotalAmount.getText().toString().trim();

            // Check if any fill is empty
            if (strPurpose.isEmpty() || strPayer.isEmpty() || strTotalAmount.isEmpty()) {
                Toast.makeText(this, "Please fill in all!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (breakDownMethod == 0) {
                Toast.makeText(this, "Please select a break down method!", Toast.LENGTH_SHORT).show();
                return false;
            }

            double totalAmountOwed = 0;
            totalAmount = Double.parseDouble(strTotalAmount);

            String owerName = "";
            String amountOwed = "";
            int owerDataListSize = DataRecyclerViewAdapter.owerDataList.size();

            for (int i = 0; i < owerDataListSize; i++) {
                Ower ower = DataRecyclerViewAdapter.owerDataList.get(i);

                totalAmountOwed += ower.getAmount();
                owerName += ower.getName();
                amountOwed += MainActivity.decimalFormat.format(ower.getAmount());

                if (i < owerDataListSize - 1) {
                    owerName += ",";
                    amountOwed += ",";
                }
            }

            // Check if the split amount is the same as the total amount
            if (totalAmountOwed < totalAmount) {
                Toast.makeText(this, "The split amount is less than the total amount!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (totalAmountOwed > totalAmount) {
                Toast.makeText(this, "The split amount is greater than the total amount!", Toast.LENGTH_SHORT).show();
                return false;
            }

            mySQLiteAdapter = new SQLiteAdapter(this);
            mySQLiteAdapter.openToRead();

            long insertedRowId = mySQLiteAdapter.insertExpense(strPurpose, strPayer, Float.parseFloat(strTotalAmount), breakDownMethod + "", owerName, amountOwed, tvSelectedDate.getText().toString());

            if (insertedRowId != -1) {
                Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
                resetOwer();
                intent = new Intent(NewExpense.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
            }

            mySQLiteAdapter.close();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position,long id) {
        strTotalAmount = editTotalAmount.getText().toString().trim();

        if (strTotalAmount == null || strTotalAmount.isEmpty())
            totalAmount = 0;
        else
            totalAmount = Double.parseDouble(strTotalAmount);

        switch (position) {
            case 1:
            case 2:
            case 3:
                breakDownMethod = position;
                intent = new Intent(NewExpense.this, Split.class);
                intent.putExtra("TOTAL_AMOUNT", totalAmount);
                intent.putExtra("LAYOUT_TYPE", position + 2);
                startActivityForResult(intent, REQUEST_CODE); // Use startActivityForResult
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    // To receive updated data from Split activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("owerDataList")) {
                List<Ower> owerDataList = (List<Ower>) data.getSerializableExtra("owerDataList");
                List<Ower> filteredDataList = new ArrayList<>();

                // Filter the owerDataList to only include elements with amount > 0
                for (Ower ower : owerDataList) {
                    if (ower.getAmount() > 0) {
                        filteredDataList.add(ower);
                    }
                }

                rvOwe = findViewById(R.id.rvOwe);
                rvOwe.setLayoutManager(new LinearLayoutManager(this));

                adapter = new DataRecyclerViewAdapter(filteredDataList, 2); // Pass the data to the adapter
                rvOwe.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }
        }
    }

    // Reset all variables to their initial values
    public void resetOwer() {
        DataRecyclerViewAdapter.dataList.clear();
        Ower.resetCheckedCount();
        Ower.resetTotalRatio();
    }

    @Override
    public boolean onSupportNavigateUp() {
        resetOwer();

        // Handle back navigation with data
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }
}