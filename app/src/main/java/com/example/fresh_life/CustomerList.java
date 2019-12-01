package com.example.fresh_life;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomerList extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        db = new DatabaseHelper(this) ;



        AutoCompleteTextView Info = findViewById(R.id.AutoCustomer);

            ArrayAdapter<Customer> arrayAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    db.getCustomers() );

            Info.setAdapter(arrayAdapter);

            Info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Customer customer = (Customer) adapterView.getAdapter().getItem(i);
                    showAddItemDialog(CustomerList.this, customer);
                }
            });

    }

    private void showAddItemDialog(Context c, final Customer customer) {
        final EditText customer_name = new EditText(c);
        customer_name.setText(String.valueOf(customer.getName()));
        final EditText customer_surname = new EditText(c);
        customer_surname.setText(String.valueOf(customer.getSurname()));
        final EditText bonus_card = new EditText(c);
        bonus_card.setText(String.valueOf(customer.getBonus_card()));
        final EditText customer_address = new EditText(c);
        customer_address.setText(String.valueOf(customer.getAddress()));
        final EditText customer_phone = new EditText(c);
        customer_phone.setText(String.valueOf(customer.getPhone()));
        LinearLayout layout = new LinearLayout(c);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(customer_name);
        layout.addView(customer_surname);
        layout.addView(customer_address);
        layout.addView(customer_phone);
        layout.addView(bonus_card);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Καρτελάκι")
                .setMessage("Ενημέρωση(Καρτελάκι)")
                .setView(layout)
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int customer_id = customer.getCustomer_id();
                        db.delete_customer(customer_id);
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCard = String.valueOf(bonus_card.getText()).trim();
                        int bonus_card = Integer.valueOf(newCard);
                        db.update_bonus_card(bonus_card, customer);
                        String new_name = String.valueOf(customer_name.getText()).trim();
                        db.update_name(new_name, customer);
                        String new_surname = String.valueOf(customer_surname.getText()).trim();
                        db.update_surname(new_surname, customer);
                        String new_address = String.valueOf(customer_address.getText()).trim();
                        db.update_address(new_address, customer);
                        String new_phone = String.valueOf(customer_phone.getText()).trim();
                        db.update_phone(new_phone, customer);
                        db.update_order(db.getCustomerOrders(customer),customer);
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }
}
