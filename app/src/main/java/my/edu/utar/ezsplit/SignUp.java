package my.edu.utar.ezsplit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;
    private Button btnSignUp;
    private EditText editUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Hide the action bar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();


        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();

        btnSignUp = findViewById(R.id.btnSignUp);
        editUserName = findViewById(R.id.editUserName);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editUserName.getText().toString().trim();

                if (!inputText.isEmpty()) {
                    // Insert the user's name into the database
                    long insertedRowId = mySQLiteAdapter.insertMember(inputText);

                    if (insertedRowId != -1) {
                        mySQLiteAdapter.close();
                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUp.this, "Failed to add you", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUp.this, "Please fill in your name", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}