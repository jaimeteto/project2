package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class placeOrder extends AppCompatActivity {
    private Spinner spinner;
    List<String[]> currentOrder= new ArrayList<>(); //saves id and quantity index 0 is id index 1 quantity
    DatabaseOpenHelper dbHelper3;
    Cursor cursor;
    SimpleCursorAdapter cursorAdapter2;
    ListView listView;
    ArrayList orderItems;
    ArrayAdapter listViewAdapter;
    TextView totalTextView;
    EditText quantityEditText;
    Double total;
    int tempqyt;
    ArrayList<String> partNames;
    ArrayAdapter<String>dataAdapter;
    private void loadSpinnerData() {

        Cursor cursor = dbHelper3.readAll();

        // Convert the cursor data into an array
        partNames = new ArrayList<>(cursor.getCount());
        int i = 0;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            partNames.add( "Part Name: "+ cursor.getString(cursor.getColumnIndexOrThrow("partName"))+ "                      Part ID: "+Integer.toString(id));
            i++;
        }

            dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, partNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
    }
    private void deleteItem(int position) {
        // Check if the position is valid
        if (position >= 0 && position < orderItems.size()) {
            String itemToDelete = orderItems.get(position).toString(); // Get the item as a string

            // Remove the item from your ArrayList
            orderItems.remove(position);

            // Notify the adapter to update the ListView
            ((ArrayAdapter<String>) listView.getAdapter()).notifyDataSetChanged();

            String[] parts = itemToDelete.split("\\|"); // Split the string
            if (parts.length >= 4) {
                String name = parts[0];
                String idString = parts[1].substring(parts[1].indexOf("ID:") + 4).trim();
                String quantity = parts[2].substring(parts[2].indexOf("Quantity:") + 9).trim();
                String price2 = parts[3].substring(parts[3].indexOf("Price per Unit:") + 15).trim();

                // get the total that was added for that entry
                int qty = Integer.parseInt(quantity);
                double pri = Double.parseDouble(price2);
                double entryTotal = qty * pri;
                total -= entryTotal;
                double totalDouble = Math.round(total * 100.0) / 100.0;
                totalTextView.setText("Total:$" + totalDouble);

            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        dbHelper3 = new DatabaseOpenHelper(this);
        spinner = findViewById(R.id.spinner);
        cursor = dbHelper3.readAll();
        listView = findViewById(R.id.listView);
        orderItems = new ArrayList<>();
        listViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderItems);
        listView.setAdapter(listViewAdapter);
        quantityEditText = findViewById(R.id.quantityEditTextPlaceOrder);
        total = 0.0;
        totalTextView = findViewById(R.id.total);


        loadSpinnerData();
        Button addToOrderButton = findViewById(R.id.addToOrder);
        Button placeOrderButton = findViewById(R.id.placeOrder);



        // Set the item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(placeOrder.this);
                builder.setTitle("Delete Entry")
                        .setItems(new String[]{"Yes", "No"}, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        //delete entry and set subtract from total
                                        deleteItem(position);
                                        break;
                                    case 1:
                                        // do nothing
                                        break;
                                }
                            }
                        });
                builder.create().show();
            }});
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {


                                                    if(currentOrder.size()==0) {
                                                        String message= "order is empty";
                                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                    //get the currenOrder list and update database
                                                    for (String[] data : currentOrder) {
                                                        String key = data[0];
                                                        String quantityString = data[1];
                                                        String pName = data[3];

                                                        Cursor cursor3 = dbHelper3.getQuantity(key);
                                                        String q="";

                                                        if (cursor3 != null) {
                                                            if (cursor3.moveToFirst()) {
                                                                q = cursor3.getString(cursor3.getColumnIndexOrThrow("quantity"));
                                                                // You have the price for the specified key
                                                            }

                                                        }
                                                        else{
                                                            q = "0";
                                                        }
                                                        int currentqty = Integer.parseInt(q);

                                                        int qty = Integer.parseInt(quantityString);

                                                        int newqty = currentqty-qty;
                                                        //check if quantity in database satisfies the quantity in placed in the order





                                                        // Use the DatabaseHelper's update method to update the database
                                                        dbHelper3.update(key,"",Integer.toString(newqty),"");



                                                        //System.out.println("Updated: Key " + key + " with Quantity " + quantityString);


                                                        cursor3.close();
                                                    }

                                                        String message= "Thank you for your order";
                                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                                        total = 0.0;
                                                        totalTextView.setText("Total:$"+total);

                                                        dataAdapter.clear();

                                                        loadSpinnerData();
                                                        dataAdapter.notifyDataSetChanged();

                                                        listViewAdapter.clear();
                                                        listViewAdapter.notifyDataSetChanged();


                                                }
                                            }
        });
        addToOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected item from the Spinner
                String selectedItem = spinner.getSelectedItem().toString();


                //get the id from the selected item
                int idIndex = selectedItem.indexOf("ID:");
                int partNameIndex = selectedItem.indexOf(":");
                
                String idString = selectedItem.substring(idIndex+3).trim();
                //display part name only

                String name = selectedItem.substring(partNameIndex+1,idIndex-5).trim();



                // Get the quantity from the EditText
                String quantity = quantityEditText.getText().toString();
                //get the price

                if(quantity.equals("")){
                    String message= "quantity is empty";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }

                else {
                    Cursor cursor2 = dbHelper3.getPrice(idString);
                    String price2 = "";

                    if (cursor2 != null) {
                        if (cursor2.moveToFirst()) {
                            price2 = cursor2.getString(cursor2.getColumnIndexOrThrow("price"));
                            // You have the price for the specified key
                        }
                        cursor2.close();
                    } else {
                        price2 = "null";
                    }

                    //add to currentOrder
                    String[] array1 = {idString, quantity, price2, name};


                    Cursor cursor4 = dbHelper3.getQuantity(idString);
                    String q = "";

                    if (cursor4 != null) {
                        if (cursor4.moveToFirst()) {
                            q = cursor4.getString(cursor4.getColumnIndexOrThrow("quantity"));
                            // You have the price for the specified key
                        }

                    } else {
                        q = "0";
                    }
                    int currentqty = Integer.parseInt(q);
                    int inputqty = Integer.parseInt(quantity);
                    //check if quantity in database satisfies the quantity in placed in the order

                    if (currentqty < inputqty) {

                        String message = "not sufficient: [" + name + "]  | available qty: " + currentqty;
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    }
                    else {

                        currentOrder.add(array1);

                        // Create a string to represent the order item
                        String orderItem = name + "| ID:" + idString + "| Quantity: " + quantity + "| Price per Unit:" + price2;

                        // Add the order item to the list and update the ListView
                        orderItems.add(orderItem);

                        //
                        listViewAdapter.notifyDataSetChanged();

                        //change total
                        double convertedDouble = Double.parseDouble(price2.trim());
                        double roundedDouble = Math.round(convertedDouble * 100.0) / 100.0; // Round to two decimal points

                        // Convert the string to an integer
                        int convertedInteger = Integer.parseInt(quantity);

                        // Multiply the double by the integer
                        double result = roundedDouble * convertedInteger;

                        total += result;
                        double totalDouble = Math.round(total * 100.0) / 100.0;
                        totalTextView.setText("Total:$" + totalDouble);
                        int selectedPosition = spinner.getSelectedItemPosition();
                        partNames.remove(selectedPosition);
                        dataAdapter.notifyDataSetChanged();


                    }
                }
            }
        });

    }
}