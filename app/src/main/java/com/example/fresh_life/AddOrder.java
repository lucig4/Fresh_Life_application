package com.example.fresh_life;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class AddOrder extends AppCompatActivity {

    DatabaseHelper db;
    Customer value;
    int customer_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        db = new DatabaseHelper(this) ;
        AutoCompleteTextView Info = findViewById(R.id.actv);

        ArrayAdapter<Customer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.getCustomers());
        Info.setAdapter(adapter);

        final EditText comments_txt_view = findViewById(R.id.comments_txt_view);



        Info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               value = (Customer) adapterView.getItemAtPosition(i);
               customer_id = value.getCustomer_id();
            }
        });

        int bonus = 0;
        bonus = getIntent().getIntExtra("bonus_card", 0);
        final int bonus_card = bonus;
        Context context = getApplicationContext();
        CharSequence text = String.valueOf(bonus_card);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        ArrayList<ChosenItem> items_selected;
        items_selected = (ArrayList<ChosenItem>) getIntent().getSerializableExtra("chosen_items");
        final ArrayList<ChosenItem> chosen_items = items_selected;


        Button searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!db.customerExists(String.valueOf(customer_id))){
                    Context context = getApplicationContext();
                    CharSequence text = "Customer new";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else exists(customer_id, chosen_items, comments_txt_view.getText().toString(), bonus_card);
            }
        });

        Button new_customer_button = findViewById(R.id.new_customer_button);

        new_customer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Intent i = new Intent(getApplicationContext(), AddCustomer.class);
                i.putExtra("chosen_items", chosen_items);
                i.putExtra("comments", comments_txt_view.getText().toString());
                i.putExtra("bonus_card", bonus_card);
                startActivity(i);

            }
        });




    }

    void exists(int value , ArrayList<ChosenItem> chosen_items, String comments, int bonus_card){


        if (db.customerExists(String.valueOf(customer_id))){
            //new order with "value" params

            newData(db.getCustomer(value), chosen_items, comments, bonus_card);

        }
        else {
            //new order AND new customer

            Context context = getApplicationContext();
            CharSequence text = "Customer new";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
    }

    void newData(Customer customer, ArrayList<ChosenItem> items_selected, String comments, int bonus_card){

        int new_bonus_card = bonus_card + customer.getBonus_card();
        if (db.update_bonus_card(new_bonus_card, customer)) {
            if (db.insertNewOrder(customer, comments, items_selected)) {

                int i;

                for (i = 0; i < items_selected.size(); i++) {
                    Item item = new Item(items_selected.get(i).getCategory(), items_selected.get(i).getItem(), items_selected.get(i).getPrice());
                    String comment = items_selected.get(i).getComments();
                    // do something with key and/or tab
                    if (db.insertNewOrderItem(db.getOrder_Id(customer.getCustomer_id()), item, comment)) {

                        Intent y = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(y);
                    }
                }


            }


        }else{
            Context context = getApplicationContext();
            CharSequence text = "Error with bonus_card";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    private final ArrayList<Customer> names(){
        ArrayList<Customer> customers = db.getCustomers();
        ArrayList<String> names = new ArrayList<>();
        int i;
        for (i=0;i<customers.size();i++){
            names.add(customers.get(i).getCustomer_id() + "/ " + customers.get(i).getName() + " " + customers.get(i).getSurname()+ "/ " + customers.get(i).getAddress()+ "/ " + customers.get(i).getPhone());
        }
        return customers;
    }



}


/*

    CharSequence text;
    Intent in = getIntent();
    final String customer_order_id = in.getStringExtra("customer_order_id");

                if (db.insertNewOrderItem(customer_order_id ,item.getItem())){
                        text = item.toString();
                        }else{
                        text = "cant add item";
                        }
                        Context context = getApplicationContext();

                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
//Intent y = new Intent(getApplicationContext(), MainActivity.class);
//startActivity(y);
*/