package com.example.fresh_life;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;


public class ChooseItems extends AppCompatActivity {

    DatabaseHelper db;
    int bonus_card = 0;
    MyExpandableListAdapter expandableListAdapter;
    ArrayList<String> expandableListTitle;
    HashMap<String,ArrayList<Item>> expandableListDetail;
    ExpandableListDataPump expandableListDataPump;
    String comments = "";
    final ArrayList<ChosenItem> chosen_items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_items);
        db = new DatabaseHelper(this) ;
        final ExpandableListView items_list = findViewById(R.id.items);


        expandableListDetail = expandableListDataPump.getData(db.getItems(), db.getItemCategories());
        expandableListTitle = new ArrayList(expandableListDetail.keySet());
        expandableListAdapter = new MyExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        items_list.setAdapter(expandableListAdapter);

        items_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });




             items_list.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {


                }
            });

        items_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                int groupPosition, int childPosition, long id) {

                    Item item = (Item) expandableListAdapter.getChild(groupPosition, childPosition);
                    if (item.getCategory().equals("καφέδες")){
                        showAddCoffeeItemDialog(ChooseItems.this,childPosition, groupPosition);
                    }else showAddItemDialog(ChooseItems.this,childPosition, groupPosition);
                    return false;
                }
            });



            Button finish_button = findViewById(R.id.finish);

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                    Intent i = new Intent(getApplicationContext(), AddOrder.class);
                    i.putExtra("chosen_items", chosen_items);
                    i.putExtra("bonus_card", bonus_card);


                    startActivity(i);

            }
        });

/*        ArrayAdapter<Item> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                db.getItems() );

        items_list.setAdapter(arrayAdapter);
*/



    }

    @Override
    public void onBackPressed() {
        // your code.
        chosen_items.clear();
        comments ="";
        bonus_card=0;
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);


    }

    private void showAddItemDialog(Context c, final int childPosition , final int groupPosition) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Σχόλια")
                .setMessage("Προσθήκη σχολίων(Προαιρετικά)")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        comments += String.valueOf(taskEditText.getText());
                        //Item item = (Item) expandableListAdapter.getChild(groupPosition, childPosition);
                        //ChosenItem chosenItem = new ChosenItem(item, comments);
                        //chosen_items.add(chosenItem);
                        //comments = "";
                        showQuantityDialog(ChooseItems.this,childPosition, groupPosition);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    private void showAddCoffeeItemDialog(Context c, final int childPosition , final int groupPosition) {

        final ArrayList<String> sugar = new ArrayList<>();

        sugar.add("Γλυκός");
        sugar.add("Μέτριος");
        sugar.add("Σκέτος");

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Ζάχαρη")
                .setItems(R.array.string_array_sugar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                comments += sugar.get(which) + " ";

                showAddItemDialog(ChooseItems.this,childPosition, groupPosition);
            }
        });
        builder.create();
        builder.show();

    }


    private void showQuantityDialog(Context c, final int childPosition , final int groupPosition) {
        final EditText quantity = new EditText(c);
        quantity.setText("1");
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Ποσότητα")
                .setMessage("")
                .setView(quantity)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String num_of_items = String.valueOf(quantity.getText());
                        int items = Integer.valueOf(num_of_items);
                        Item item = (Item) expandableListAdapter.getChild(groupPosition, childPosition);
                        ChosenItem chosenItem = new ChosenItem(item, comments);
                        for (int i=0;i<items;i++) {
                            chosen_items.add(chosenItem);
                            if (item.getCategory().equals("καφέδες")){
                                bonus_card ++;
                            }
                        }
                        comments = "";
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }
}
