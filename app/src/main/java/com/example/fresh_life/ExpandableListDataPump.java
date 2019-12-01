package com.example.fresh_life;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {



    public static HashMap<String, ArrayList<Item>> getData(ArrayList<Item> items, ArrayList<String> categories) {

        HashMap<String,ArrayList<Item>> expandableListDetail = new HashMap<>();
        int i;
        ArrayList<Item> items_names;
        int j;
        for (j=0;j<categories.size();j++){
            items_names = new ArrayList<>();

            for (i=0;i<items.size();i++){
                    if (categories.get(j).equals(items.get(i).getCategory()))
                    items_names.add(items.get(i));

            }

            expandableListDetail.put(categories.get(j), items_names);

        }

        return expandableListDetail;
    }
}

