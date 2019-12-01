package com.example.fresh_life;

public class OrderItem {
    public int order_item_id;
    public String item;
    public int order_id;
    public String category;
    public double price;
    public String comment;

    public OrderItem(int order_id, String item, String category, double price, String comment, int order_item_id){
        this.item = item;
        this.order_id = order_id;
        this.category = category;
        this.price = price;
        this.comment = comment;
        this.order_item_id = order_item_id;

    }

    public String getItem() {
        return item;
    }

    public int getOrder_id(){
        return order_id;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getComment() {
        return comment;
    }

    public int getOrder_item_id() {
        return order_item_id;
    }

    @Override
    public String toString() {
        return item;
    }
}
