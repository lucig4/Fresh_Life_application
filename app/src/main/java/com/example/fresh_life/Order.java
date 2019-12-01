package com.example.fresh_life;


import java.util.ArrayList;

public class Order {
    DatabaseHelper db;

    public String name;
    public String surname;
    public String address;
    public String phone;
    public int bonus_card;
    public int customer_id;
    public String order_id;
    public String comments;
    public double total_price;



    public Order(String name, String surname, String address, String phone, int bonus_card, int customer_id, String order_id, String comments, double total_price) {
        addname(name);
        addsurname(surname);
        addaddress(address);
        addphone(phone);
        addbonus_card(bonus_card);
        addcustomer_id(customer_id);
        addorder_id(order_id);
        addcomments(comments);
        addtotal_price(total_price);

    }

    public void addname(String name){
        this.name = name;
    }
    public void addsurname(String surname){
        this.surname = surname;
    }
    public void addaddress(String address){
        this.address = address;
    }
    public void addphone(String phone){
        this.phone = phone;
    }
    public void addbonus_card(int bonus_card){
        this.bonus_card = bonus_card;
    }
    public void addcustomer_id(int customer_id){
        this.customer_id = customer_id;
    }
    public void addorder_id(String order_id){
        this.order_id = order_id;
    }
    public void addcomments(String comments){ this.comments = comments; }
    public void addtotal_price(double total_price){ this.total_price = total_price;}

    public String getName() { return name; }
    public String getSurname() {
        return surname;
    }
    public String getAddress() {
        return address;
    }
    public String getPhone() {
        return phone;
    }
    public int getCustomer_id() {
        return customer_id;
    }
    public int getBonus_card() {
        return bonus_card;
    }
    public String getOrder_id(){ return  order_id;}
    public String getComments(){ return  comments;}
    public double getTotal_price(){ return total_price;}

    @Override
    public String toString(){
        String output = this.getName() + " " + this.getSurname() + " " + this.getAddress() + " " + this.getPhone();
        return output;
    }


    public String fullOrder(){
        String output = this.getName() + " " + this.getSurname() + " ";
        ArrayList<OrderItem> order_items;
        order_items = db.getOrderItems(this.getOrder_id());
        int i;
        for (i=0;i<order_items.size();i++){
            output += order_items.get(i).getItem() + " ";
        }

        return output;
    }
}
