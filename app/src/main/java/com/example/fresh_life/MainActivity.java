package com.example.fresh_life;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this) ;
        final ListView orders_list = findViewById(R.id.orders);
        final TextView show_order = findViewById(R.id.show_order);
        show_order.setMovementMethod(new ScrollingMovementMethod());



        Button customer_list_button = findViewById(R.id.customer_list_button);
        customer_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                Intent i = new Intent(getApplicationContext(), CustomerList.class);
                startActivity(i);



            }
        });

        if (db.orderExists())
        {
            // This is the array adapter, it takes the context of the activity as a
            // first parameter, the type of list view as a second parameter and your
            // array as a third parameter.

            ArrayAdapter<Order> arrayAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    db.getOrders() );

            orders_list.setAdapter(arrayAdapter);
        }
        if (db.orderExists()) {
            Order order = db.getOrders().get(0);
            ArrayList<OrderItem> order_items = db.getOrderItems(order.getOrder_id());
            int k;
            String text = order.toString() + "\n";
            text += "Bonus καρτελάκι: " + db.getCustomer(order.getCustomer_id()).getBonus_card() + "\n";
            for (k = 0; k < order_items.size(); k++) {
                text += order_items.get(k).getItem() + " (" + order_items.get(k).getComment() + ")"+ "/"+ order_items.get(k).getPrice()+ " €, \n";
            }

            if(order.getComments().equals(null)){}else text +=order.getComments();
            text +="Σύνολο: " + order.getTotal_price() + " €";

            show_order.setText(text);
        }else show_order.setText("no orders");

        orders_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Order order = (Order) adapterView.getAdapter().getItem(i);

                ArrayList<OrderItem> order_items = db.getOrderItems(order.getOrder_id());
                int k;
                String text = order.toString() + "\n";
                text += "Bonus καρτελάκι: " + db.getCustomer(order.getCustomer_id()).getBonus_card() + "\n";
                for (k = 0; k < order_items.size(); k++) {
                    text += order_items.get(k).getItem() + " (" + order_items.get(k).getComment() + ")"+ "/"+ order_items.get(k).getPrice()+ ", \n";
                }


                if(order.getComments().equals(null)){}else text +=order.getComments();


                text +="Σύνολο: " + order.getTotal_price() + " €";

                show_order.setText(text);

 

                //Intent y = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(y);
            }
        });

        Button add =  findViewById(R.id.add_button);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                Intent i = new Intent(getApplicationContext(), ChooseItems.class);
                startActivity(i);



            }
        });

        Button sync_button = findViewById(R.id.sync);

        sync_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Eπιβεβαίωση")
                        .setMessage("Θα γίνει διαγραφή δεδομένων.(Βεβαιωθείτε οτι εγινε αποθήκευση των δεδομένων)")
                        .setPositiveButton("Επιβεβαίωση", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                SyncData syncData = new SyncData(getApplicationContext());


                                syncData.sync();
                                Toast.makeText(MainActivity.this, "Περιμένετε να γίνει ο συγχρονισμος",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();



            }
        });

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncData syncData = new SyncData(getApplicationContext());
                syncData.save();
            }
        });


        orders_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Eπιβεβαίωση")
                        .setMessage("Θα γίνει διαγραφή δεδομένων.")
                        .setPositiveButton("Διαγραφή", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                Order order = (Order) adapterView.getAdapter().getItem(i);

                                db.delete_order(order.getOrder_id());
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton("Ακύρωση", null)
                        .create();
                dialog.show();



                return false;
            }
        });
    }



}
