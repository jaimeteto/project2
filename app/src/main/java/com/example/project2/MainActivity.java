package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnInventory = findViewById(R.id.btnInventory);
        Button btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        // Add an OnClickListener for the "Inventory" button
        btnInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the Inventory activity
                Intent intent = new Intent(MainActivity.this, Inventory.class);
                startActivity(intent);
            }
        });

        // Add an OnClickListener for the "Place an Order" button
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent to open placeOrder
                Intent intent = new Intent(MainActivity.this, placeOrder.class);
                startActivity(intent);


            }
        });
    }
}