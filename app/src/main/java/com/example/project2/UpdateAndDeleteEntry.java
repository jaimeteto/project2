package com.example.project2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateAndDeleteEntry extends AppCompatActivity {
    DatabaseOpenHelper dbHelper2;
    String id="";
    String carInfo;
    String qty;
    String price;
    EditText carInfoInput;
    EditText qtyInput;

    EditText priceInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_and_delete_entry);
        Button deleteEntryButton = findViewById(R.id.deleteEntry);
        Button updateEntryButton = findViewById(R.id.update);
        Button cancelEntryButton = findViewById(R.id.cancelUpdate);
        carInfoInput= findViewById(R.id.carInfoUpdate);
        qtyInput = findViewById(R.id.quantityUpdate);
        priceInput = findViewById(R.id.priceUpdate);
        dbHelper2=new DatabaseOpenHelper(this);
        //get data from caller
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("message_key");

        }

        cancelEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();



            }


        });
        //listener for update button
        updateEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get all the new info
                carInfo = carInfoInput.getText().toString();
                qty= qtyInput.getText().toString();
                price = priceInput.getText().toString();

                //update in the data base

                if(carInfo.equals("")&& qty.equals("")&&price.equals("")){
                    String message= "nothing to update";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }
                else {
                    dbHelper2.update(id, carInfo, qty, price);

                    //finish activity
                    finish();
                }
            }
        });

        //listener for delete button
        deleteEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete entry from database using the dialog box

                alertView("DELETE ENTRY",id);
            }
        });
    }
    private void alertView(String message ,String entryId ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateAndDeleteEntry.this);
        dialog.setTitle( message )
                .setIcon(R.drawable.ic_launcher_background)
                .setMessage("Are you sure you want to delete this entry?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }})
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {

                        dbHelper2.delete(entryId);
                        finish();

                    }
                }).show();
    }
}