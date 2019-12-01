package com.example.fresh_life;


import java.io.Serializable;

public class Item implements Serializable {
    public String category;
    public String item;
    public String price;



    public Item(String category, String item, String price) {
        addcategory(category);
        additem(item);
        addprice(price);

    }

    public void addcategory(String category){
        this.category = category;
    }
    public void additem(String item){
        this.item = item;
    }
    public void addprice(String price){
        this.price = price;
    }


    public String getCategory() { return category; }
    public String getItem() {
        return item;
    }
    public String getPrice() {
        return price;
    }

    @Override
    public String toString(){
        String output = this.getItem() + " " + this.getPrice();
        return output;
    }

    public boolean equals(Item item1){
        if (item1.getItem() == this.getItem())
        {
            return true;
        }
        else{
            return false;
        }
    }


}

