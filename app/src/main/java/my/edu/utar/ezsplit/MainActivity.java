package my.edu.utar.ezsplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    private RecyclerView rvExpense;

    private TextView tvNoExpense;

    private DataRecyclerViewAdapter adapter;
    private SQLiteAdapter mySQLiteAdapter;

    private static final int REQUEST_CODE = 1;

    public static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listExpense();


        fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewExpense.class);
            startActivity(intent);
        });
    }

    // To call the designed option menu from the menu folder
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // To define the listener for menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_member) {
            Intent intent = new Intent(this, Members.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void listExpense() {
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        rvExpense = findViewById(R.id.rvExpense);
        rvExpense.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve data from database
        List<Expense> expenseList = mySQLiteAdapter.queueAllExpense();

        tvNoExpense = findViewById(R.id.tvNoExpense);

        if (expenseList.isEmpty()) {
            tvNoExpense.setVisibility(View.VISIBLE);
        } else {
            tvNoExpense.setVisibility(View.GONE);

            adapter = new DataRecyclerViewAdapter(MainActivity.this, expenseList); // Pass the data to the adapter
            rvExpense.setAdapter(adapter);
        }

        mySQLiteAdapter.close();
    }

    // To receive updated data from NewExpense activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
            listExpense();
    }
}