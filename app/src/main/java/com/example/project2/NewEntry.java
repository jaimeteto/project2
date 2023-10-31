package com.example.project2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewEntry extends AppCompatActivity {
    private EditText partNameEntry;
    private EditText quantityEntry;
    private EditText carInfoEntry;

    private EditText priceEntry;

    DatabaseOpenHelper dbHelper2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        Button cancelButton = findViewById(R.id.cancelEntry);
        Button addButton = findViewById(R.id.addEntry);

        partNameEntry = findViewById(R.id.partNameEntry);
        quantityEntry = findViewById(R.id.quantityEntry);
        carInfoEntry = findViewById(R.id.carInfoEntry);
        priceEntry = findViewById(R.id.priceEntry);

        dbHelper2= new DatabaseOpenHelper(this);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Finish the current activity and return to the previous one
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                String partNameString = partNameEntry.getText().toString();
                String tempqty = quantityEntry.getText().toString();
                String priceString = priceEntry.getText().toString();
                if (partNameString.equals("") || tempqty.equals("")||priceString.equals("")){
                    String message= "Part Name, qty and price cannot be emtpy";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }


                else {

                    int quantity = Integer.parseInt(tempqty);
                    String carInfoString = carInfoEntry.getText().toString();



                    //add data to database
                    dbHelper2.insert(partNameString, quantity, carInfoString, priceString);
                    finish();

                }
            }
        });
    }

}