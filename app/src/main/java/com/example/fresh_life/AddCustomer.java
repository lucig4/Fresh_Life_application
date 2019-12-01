package com.example.fresh_life;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddCustomer extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        db = new DatabaseHelper(this) ;

        ArrayList<ChosenItem> items_selected;
        items_selected = (ArrayList<ChosenItem>) getIntent().getSerializableExtra("chosen_items");
        final ArrayList<ChosenItem> chosen_items = items_selected;
        final String comments = getIntent().getStringExtra("comments");
        int bonus = 0;
        bonus = getIntent().getIntExtra("bonus_card", 0);
        final int bonus_card = bonus;

        Button searchButton = findViewById(R.id.add_customer_button);


        searchButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        newData(chosen_items, comments, bonus_card);
                    }
                });
    }

    void newData(ArrayList<ChosenItem> chosen_items, String comments, int bonus_card){
        EditText Name  = findViewById(R.id.name);
        String customer_name = Name.getText().toString();

        EditText Surname  = findViewById(R.id.surname);
        String customer_surname = Surname.getText().toString();

        EditText Address  = findViewById(R.id.address);
        String customer_address = Address.getText().toString();

        EditText Phone  = findViewById(R.id.phone);
        String customer_phone = Phone.getText().toString();

        //EditText Bonus = findViewById(R.id.bonus_card);

        long id = db.insertNewCustomer(customer_name, customer_surname, customer_address, customer_phone , bonus_card);
        if (id != -1){
            Customer customer = db.getCustomer((int) id);
            if (db.insertNewOrder(customer, comments, chosen_items)){
                int i;

                for ( i=0;i<chosen_items.size();i++) {
                    Item item = new Item(chosen_items.get(i).getCategory(), chosen_items.get(i).getItem(), chosen_items.get(i).getPrice());
                    String comment = chosen_items.get(i).getComments();
                    // do something with key and/or tab
                    if (db.insertNewOrderItem(db.getOrder_Id(customer.getCustomer_id()), item, comment)) {

                        Intent y = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(y);
                    }else {
                        Context context = getApplicationContext();


                        String text= "cant insert new order items";

                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }






            }
        }




    }
}
