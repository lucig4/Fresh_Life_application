package com.example.fresh_life;

public class Customer {
    public String name;
    public String surname;
    public String address;
    public String phone;
    public int bonus_card;
    public int customer_id;


    public Customer(String name, String surname, String address, String phone, int bonus_card, int customer_id) {
        addname(name);
        addsurname(surname);
        addaddress(address);
        addphone(phone);
        addbonus_card(bonus_card);
        addcustomer_id(customer_id);

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

    @Override
    public String toString(){
        String output = getCustomer_id() + "/ " + getName() + " " + getSurname()+ "/ " + getAddress()+ "/ " + getPhone();
        return output;
    }


}
