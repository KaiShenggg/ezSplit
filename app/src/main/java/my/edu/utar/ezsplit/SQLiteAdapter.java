package my.edu.utar.ezsplit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLiteAdapter {

    // SQL Commands
    // Constant variables
    private static final String MYDATABASE_NAME = "MY_DATABASE";
    public static final int MYDATABASE_VERSION = 1;

    private static final String DATABASE_MEMBER_TABLE = "MY_MEMBER";
    private static final String MEMBER_KEY_CONTENT = "Member_Name";

    private static final String DATABASE_EXPENSE_TABLE = "MY_EXPENSE";
    private static final String EXPENSE_KEY_CONTENT = "Purpose";
    private static final String EXPENSE_KEY_CONTENT_2 = "Payer";
    private static final String EXPENSE_KEY_CONTENT_3 = "Total_Amount";
    private static final String EXPENSE_KEY_CONTENT_4 = "Break_Down_Method";
    private static final String EXPENSE_KEY_CONTENT_5 = "Ower";
    private static final String EXPENSE_KEY_CONTENT_6 = "Amount_Owed";
    private static final String EXPENSE_KEY_CONTENT_7 = "Date";

    // SQL command to create the table with the columns
    private static final String SCRIPT_CREATE_DATABASE_MEMBER_TABLE = "create table " + DATABASE_MEMBER_TABLE +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MEMBER_KEY_CONTENT + " text not null);";
    private static final String SCRIPT_CREATE_DATABASE_EXPENSE_TABLE = "create table " + DATABASE_EXPENSE_TABLE +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EXPENSE_KEY_CONTENT + " text not null, " +
            EXPENSE_KEY_CONTENT_2 + " text not null, " +
            EXPENSE_KEY_CONTENT_3 + " decimal(10,2) not null, " +
            EXPENSE_KEY_CONTENT_4 + " text not null, " +
            EXPENSE_KEY_CONTENT_5 + " text not null, " +
            EXPENSE_KEY_CONTENT_6 + " text not null, " +
            EXPENSE_KEY_CONTENT_7 + " text not null);";

    // Variables
    private Context context;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    // Constructor
    public SQLiteAdapter(Context c) {
        context = c;
    }

    // Open the database to insert data / to write data
    public SQLiteAdapter openToWrite() throws android.database.SQLException {
        // Create a table with MYDATABASE_NAME and the version of MYDATABASE_VERSION
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);

        // Open to write
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        return this;
    }

    // Open the database to read data
    public SQLiteAdapter openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);

        // Open to read
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        return this;
    }

    // Insert data into the column
    public long insertMember(String content) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEMBER_KEY_CONTENT, content);

        return sqLiteDatabase.insert(DATABASE_MEMBER_TABLE, null, contentValues);
    }

    public long insertExpense(String content, String content_2, float content_3, String content_4, String content_5, String content_6, String content_7) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXPENSE_KEY_CONTENT, content);
        contentValues.put(EXPENSE_KEY_CONTENT_2, content_2);
        contentValues.put(EXPENSE_KEY_CONTENT_3, content_3);
        contentValues.put(EXPENSE_KEY_CONTENT_4, content_4);
        contentValues.put(EXPENSE_KEY_CONTENT_5, content_5);
        contentValues.put(EXPENSE_KEY_CONTENT_6, content_6);
        contentValues.put(EXPENSE_KEY_CONTENT_7, content_7);

        return sqLiteDatabase.insert(DATABASE_EXPENSE_TABLE, null, contentValues);
    }

    // Retrieve the data from the table
    public List<Ower> queueAllMember() {
        String[] columns = new String[] {MEMBER_KEY_CONTENT};
        Cursor cursor = sqLiteDatabase.query(DATABASE_MEMBER_TABLE, columns, null, null, null, null, null);

        List<Ower> result = new ArrayList<>();
        int index_CONTENT = cursor.getColumnIndex(MEMBER_KEY_CONTENT);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
            result.add(new Ower(cursor.getString(index_CONTENT)));

        return result;
    }

    public List<Expense> queueAllExpense() {
        String[] columns = new String[] {EXPENSE_KEY_CONTENT, EXPENSE_KEY_CONTENT_2, EXPENSE_KEY_CONTENT_3, EXPENSE_KEY_CONTENT_4, EXPENSE_KEY_CONTENT_5, EXPENSE_KEY_CONTENT_6, EXPENSE_KEY_CONTENT_7};
        Cursor cursor = sqLiteDatabase.query(DATABASE_EXPENSE_TABLE, columns, null, null, null, null,  "id DESC");

        List<Expense> result = new ArrayList<>();
        int index_CONTENT = cursor.getColumnIndex(EXPENSE_KEY_CONTENT);
        int index_CONTENT_2 = cursor.getColumnIndex(EXPENSE_KEY_CONTENT_2);
        int index_CONTENT_3 = cursor.getColumnIndex(EXPENSE_KEY_CONTENT_3);
        int index_CONTENT_4 = cursor.getColumnIndex(EXPENSE_KEY_CONTENT_4);
        int index_CONTENT_5 = cursor.getColumnIndex(EXPENSE_KEY_CONTENT_5);
        int index_CONTENT_6 = cursor.getColumnIndex(EXPENSE_KEY_CONTENT_6);
        int index_CONTENT_7 = cursor.getColumnIndex(EXPENSE_KEY_CONTENT_7);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
            result.add(new Expense(cursor.getString(index_CONTENT), cursor.getString(index_CONTENT_2), cursor.getDouble(index_CONTENT_3), cursor.getString(index_CONTENT_4),
                    Arrays.asList(cursor.getString(index_CONTENT_5).split(",\\s*")), Arrays.asList(cursor.getString(index_CONTENT_6).split(",\\s*")), cursor.getString(index_CONTENT_7)));

        return result;
    }

    // Get the length of Member table
    public int getMemberLength() {
        String[] columns = new String[] {MEMBER_KEY_CONTENT};
        Cursor cursor = sqLiteDatabase.query(DATABASE_MEMBER_TABLE, columns, null, null, null, null, null);

        return cursor.getCount();
    }

    // Close the database
    public void close() {
        sqLiteHelper.close();
    }


    // Superclass of SQLiteOpenHelper ---> implement both the override methods which creates the database
    public class SQLiteHelper extends SQLiteOpenHelper {

        // Constructor with 4 parameters
        public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // To create the database
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE_MEMBER_TABLE);
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE_EXPENSE_TABLE);
        }

        // Version control
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE_MEMBER_TABLE);
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE_EXPENSE_TABLE);
        }
    }
}