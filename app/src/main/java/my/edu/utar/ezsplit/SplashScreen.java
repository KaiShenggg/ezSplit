package my.edu.utar.ezsplit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000;
    private SQLiteAdapter mySQLiteAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Hide the action bar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();


        // Retrieve member length from database
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        int memberLength = mySQLiteAdapter.getMemberLength();

        mySQLiteAdapter.close();


        // Use a Handler to post a delayed Runnable to transition to the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if the user has sign in before
                if (memberLength == 0)
                    intent = new Intent(SplashScreen.this, SignUp.class);
                else
                    intent = new Intent(SplashScreen.this, MainActivity.class);

                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }
}