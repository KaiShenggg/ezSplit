package my.edu.utar.ezsplit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Members extends AppCompatActivity {

    private CardView cvNewMember;
    private TextView txNewMember;
    private EditText editMemberName;
    private RecyclerView rvMemberName;
    private DataRecyclerViewAdapter adapter;
    private SQLiteAdapter mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        getSupportActionBar().setTitle("Members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mySQLiteAdapter = new SQLiteAdapter(this);

        listMemberName();


        // To change from TextView to EditText when the user clicks the New Member CardView
        cvNewMember = findViewById(R.id.cvNewMember);
        txNewMember = findViewById(R.id.txNewMember);
        editMemberName = findViewById(R.id.editMemberName);
        editMemberName.setVisibility(View.GONE);

        cvNewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace TextView with EditText
                txNewMember.setVisibility(View.GONE);
                editMemberName.setVisibility(View.VISIBLE);
                editMemberName.requestFocus();

                // Open the soft keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editMemberName, InputMethodManager.SHOW_IMPLICIT);

                editMemberName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        // Check if the "Done" or "Enter" key was pressed
                        if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            mySQLiteAdapter.openToWrite();

                            String inputText = editMemberName.getText().toString().trim();

                            if (!inputText.isEmpty()) {
                                // Insert the member's name into the database
                                long insertedRowId = mySQLiteAdapter.insertMember(inputText);

                                if (insertedRowId != -1) {
                                    // Hide the keyboard
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                                    // Replace back EditText with TextView
                                    txNewMember.setVisibility(View.VISIBLE);
                                    editMemberName.setVisibility(View.GONE);
                                    editMemberName.setText("");

                                    listMemberName();

                                    Toast.makeText(Members.this, "Member added", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Members.this, "Failed to add member", Toast.LENGTH_SHORT).show();
                                }
                            }
                            mySQLiteAdapter.close();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
    }

    public void listMemberName() {

        mySQLiteAdapter.openToRead();

        rvMemberName = findViewById(R.id.rvMemberName);
        rvMemberName.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve data from database
        List<Ower> memberNameList = mySQLiteAdapter.queueAllMember();
        adapter = new DataRecyclerViewAdapter(memberNameList, 1); // Pass the data to the adapter
        rvMemberName.setAdapter(adapter);

        mySQLiteAdapter.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}