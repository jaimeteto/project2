package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Inventory extends AppCompatActivity {

    private SQLiteDatabase db = null;
    SimpleCursorAdapter cursorAdapter1;
    ListView mlist;
    LoadDB task;
    ArrayAdapter myAdapter;
    Cursor mCursor;
    DatabaseOpenHelper dbHelper;
    final public static String TABLE_NAME = "parts";
    final public static String PART_NAME = " partName";
    public static String _ID = "_id";
    final public static String CAR_INFO = "carInfo";
    final public static String NAME = "inventory_db";
    final private static Integer VERSION = 1;
    final public static String QTY = "quantity";
    final public static String[] allColumns = { _ID,
            PART_NAME,QTY,CAR_INFO };
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        mlist = (ListView) findViewById(R.id.mlist);
        myAdapter = new ArrayAdapter<String>(this, R.layout.line);
        //mlist.setAdapter(myAdapter);

        dbHelper = new DatabaseOpenHelper(this);
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 2);
        addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the 'Entry' activity
                Intent intent = new Intent(Inventory.this, NewEntry.class);

                // You may pass data to the new activity using extras if needed
                // intent.putExtra("key", "value");

                // Start the 'Entry' activity
                startActivity(intent);
            }
        });


    }

    @Override
    public void onStop(){
        super.onStop();
        dbHelper.close();

    }
    @Override
    public void onResume(){
        super.onResume();

        //we open the data base in a different thread


        //create a assyntask object and then call its on backgroud fucntion to create the database
        //and return a cursor with all values
        task = new LoadDB();
        //getting the cursor with all entries
        task.execute();




    }

    public void clearEdit(View v) {
        myAdapter.clear();
        dbHelper.deleteEntries();
        //refreshing display
        cursorAdapter1.changeCursor(dbHelper.readAll());
        cursorAdapter1.notifyDataSetChanged();

    }
    private final class LoadDB extends AsyncTask<String, Void, Cursor> {
        // runs on the UI thread
        @Override protected void onPostExecute(Cursor data) {
            cursorAdapter1 = new SimpleCursorAdapter(getApplicationContext(),
                    R.layout.view1,
                    data,
                    new String[] {"_id", "partName","quantity","carInfo" },
                    new int[] {R.id.textView8, R.id.textView2,R.id.textView4,R.id.textView6},0);
            mCursor = data;
//            cursorAdapter1.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
//                @Override
//                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//                    if (view.getId() == R.id.list_item_root) {
//                        // Set the unique identifier for the list item
//                        int rowId = cursor.getInt(data.getColumnIndexOrThrow("_id"));
//                        view.setTag(rowId);
//                        return true;
//                    }
//                    return false;
//                }
//            });
            mlist.setAdapter(cursorAdapter1);
            mlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                 public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                     //open UpdateAndDelete activity
                                                     Intent intent = new Intent(Inventory.this, UpdateAndDeleteEntry.class);
//                                                     Object tag = view.getTag();
//                                                     String itemTag="";
                                                     TextView textView = view.findViewById(R.id.textView8);

                                                     // Get the text from the TextView
                                                     String text = textView.getText().toString();

                                                     // Do something with the text
                                                     // For example, you can show a toast message with the text
                                                     Toast.makeText(Inventory.this, "Clicked item text: " + text, Toast.LENGTH_SHORT).show();




                                                     intent.putExtra("message_key", text);
                                                     startActivity(intent);


                                                     return true;
                                                 }
                                             }
            );

        }
        // runs on its own thread
        @Override
        protected Cursor doInBackground(String... args) {
            //dbHelper=new DatabaseOpenHelper(getApplicationContext());
            db = dbHelper.getWritableDatabase();

            return db.query(TABLE_NAME,allColumns , null, null, null, null, null);

        }
    }
}