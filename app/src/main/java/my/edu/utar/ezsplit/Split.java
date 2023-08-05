package my.edu.utar.ezsplit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Split extends AppCompatActivity {

    private RecyclerView rvMemberName;
    private DataRecyclerViewAdapter adapter;

    private TextView tvTotalAmount;
    double totalAmount;

    int layoutType;

    int itemCount;
    double calTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get total amount
        tvTotalAmount = findViewById(R.id.tvTotalAmount_Split);

        Intent intent = getIntent();
        totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0);
        tvTotalAmount.setText("RM " + MainActivity.decimalFormat.format(totalAmount));

        layoutType = intent.getIntExtra("LAYOUT_TYPE", 0);


        listOwe(layoutType);
    }

    public void listOwe(int layoutType) {
        rvMemberName = findViewById(R.id.rvMemberName_Split);
        rvMemberName.setLayoutManager(new LinearLayoutManager(this));

        if (layoutType == 3) {
            getSupportActionBar().setTitle("Split Equally");
            adapter = new DataRecyclerViewAdapter(DataRecyclerViewAdapter.dataList, 3, totalAmount);
        } else if (layoutType == 4) {
            getSupportActionBar().setTitle("Split By Ratio");
            adapter = new DataRecyclerViewAdapter(DataRecyclerViewAdapter.dataList, 4, totalAmount);
        } else if (layoutType == 5) {
            getSupportActionBar().setTitle("Split By Amount");
            adapter = new DataRecyclerViewAdapter(DataRecyclerViewAdapter.dataList, layoutType);
        } else {
            throw new IllegalArgumentException("Invalid layout type");
        }

        rvMemberName.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        itemCount = DataRecyclerViewAdapter.dataList.size();
        calTotalAmount = 0;

        switch(layoutType) {
            case 3:
                if (Ower.getCheckedCount() == 0) {
                    Toast.makeText(this, "Must select at least one member", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case 4:
                if (Ower.getTotalRatio() == 0) {
                    Toast.makeText(this, "You have RM " + MainActivity.decimalFormat.format(totalAmount) + " left to distribute", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case 5:
                for (int i = 0; i < itemCount; i++) {
                    calTotalAmount += DataRecyclerViewAdapter.dataList.get(i).getAmount();

                    if (calTotalAmount > totalAmount) {
                        Toast.makeText(this, "The sum of each member's amount is greater than the total amount.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }

                if (calTotalAmount < totalAmount) {
                    Toast.makeText(this, "The sum of each member's amount is less than the total amount.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
        }

        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        // Handle back navigation with data
        Intent intent = new Intent().putExtra("owerDataList", new ArrayList<>(DataRecyclerViewAdapter.dataList));
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }
}