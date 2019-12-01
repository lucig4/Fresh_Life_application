package com.example.fresh_life;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;




public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "fresh_life_database";

    // Table Names
    private static final String table_customers = "customers";
    private static final String table_orders = "orders";
    private static final String table_items = "items";
    private static final String table_order_items = "order_items";

    //Common columns (customers columns)
    private static final String customer_id = "customer_id";
    private static final String name = "name";
    private static final String surname = "surname";
    private static final String address = "address";
    private static final String phone = "phone";
    private static final String bonus_card = "bonus_card";

    //orders columns
    private static final String order_id = "order_id";
    private static final String comments = "comments";
    private static final String total_price = "total_price";

    //items columns
    private static final String category = "category";
    private static final String item = "item";
    private static final String price = "price";

    //order_item column extra
    private static final String order_item_id = "order_item_id";
    private static final String comment = "comment";

    // Customers table create statement
    private static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE " + table_customers
            + "(" + customer_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + name + " VARCHAR,"
            + surname + " VARCHAR," + address + " VARCHAR," + phone + " VARCHAR," + bonus_card + " INTEGER" + ")";

    //Orders table create statement
    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + table_orders
            + "(" + customer_id + " INTEGER," + name + " VARCHAR,"
            + surname + " VARCHAR," + address + " VARCHAR," + phone + " VARCHAR," + bonus_card + " INTEGER,"
            + order_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + comments + " VARCHAR," + total_price + " DOUBLE"+")";

    //Items table create statement
    private static final String CREATE_ITEMS = "CREATE TABLE " + table_items
            + "(" + category + " VARCHAR, " + item + " VARCHAR," + price + " VARCHAR" + ")";

    private static final String CREATE_ORDER_ITEMS = "CREATE TABLE " + table_order_items
            + "("+ order_item_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + order_id + " INTEGER, " + category + " VARCHAR, " + item + " VARCHAR, " + price + " VARCHAR, " + comment + " VARCHAR" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CUSTOMERS);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_ITEMS);
        db.execSQL(CREATE_ORDER_ITEMS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_customers);
        db.execSQL("DROP TABLE IF EXISTS " + table_orders);
        db.execSQL("DROP TABLE IF EXISTS " + table_items);
        db.execSQL("DROP TABLE IF EXISTS " + table_order_items);
        onCreate(db);
    }

    // Adding new Customer
    public long insertNewCustomer(String customer_name, String customer_surname, String customer_address, String customer_phone, int bonus_card) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(name, customer_name);
        contentValues.put(surname, customer_surname);
        contentValues.put(address, customer_address);
        contentValues.put(phone, customer_phone);
        contentValues.put(this.bonus_card, bonus_card);
        long result = db.insert(table_customers, null, contentValues);

        db.close();

        return result;
    }

    ArrayList<Customer> getCustomers() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM " + table_customers;
        Cursor c = db.rawQuery(sql, null);
        Customer customer;
        c.moveToFirst();

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String customer_name = c.getString(c.getColumnIndex("name"));
                    String customer_surname = c.getString(c.getColumnIndex("surname"));
                    String customer_address = c.getString(c.getColumnIndex("address"));
                    String customer_phone = c.getString(c.getColumnIndex("phone"));
                    int customer_customer_id = c.getInt(c.getColumnIndex("customer_id"));
                    int customer_bonus_card = c.getInt(c.getColumnIndex("bonus_card"));

                    customer = new Customer(customer_name, customer_surname, customer_address, customer_phone, customer_bonus_card, customer_customer_id);
                    customers.add(customer);
                } while (c.moveToNext());
            }
        } else {
            c.close();
            db.close();
            return null;
        }

        c.close();
        db.close();
        return customers;
    }

    Customer getCustomer(int info) {

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_customers + " WHERE customer_id = '" + info + "'";
        Cursor c = db.rawQuery(sql, null);
        Customer customer;
        c.moveToFirst();

        String customer_name = c.getString(c.getColumnIndex("name"));
        String customer_surname = c.getString(c.getColumnIndex("surname"));
        String customer_address = c.getString(c.getColumnIndex("address"));
        String customer_phone = c.getString(c.getColumnIndex("phone"));
        int customer_customer_id = c.getInt(c.getColumnIndex("customer_id"));
        int customer_bonus_card = c.getInt(c.getColumnIndex("bonus_card"));

        customer = new Customer(customer_name, customer_surname, customer_address, customer_phone, customer_bonus_card, customer_customer_id);

        c.close();
        return customer;
    }

    int getCustomerId(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT customer_id FROM " + table_customers + " WHERE name = '" + name + "'";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();

        int customer_id = c.getInt(c.getColumnIndex("customer_id"));
        return customer_id;
    }

    //Customers exist
    public boolean customers_exist() {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + table_customers;
        Cursor c = db.rawQuery(sql, null);
        if (c != null) {
            if (c.moveToFirst()) {

                return true;
            }
        }
        return false;

    }

    // Adding new Order
    public boolean insertNewOrder(Customer customer, String comments, ArrayList<ChosenItem> order_items) {

        SQLiteDatabase db = this.getWritableDatabase();

        String customer_name = customer.getName();
        String customer_surname = customer.getSurname();
        String customer_address = customer.getAddress();
        String customer_phone = customer.getPhone();
        int customer_customer_id = customer.getCustomer_id();
        int customer_bonus_card = customer.getBonus_card();
        double total_price = total_price(order_items, customer_bonus_card, customer_customer_id);

        ContentValues contentValues = new ContentValues();
        contentValues.put(name, customer_name);
        contentValues.put(surname, customer_surname);
        contentValues.put(address, customer_address);
        contentValues.put(phone, customer_phone);
        contentValues.put(customer_id, customer_customer_id);
        contentValues.put(bonus_card, customer_bonus_card);
        contentValues.put(this.comments, comments);
        contentValues.put(this.total_price, total_price);
        long result = db.insert(table_orders, null, contentValues);

        db.close();
        if (result == -1) return false;
        return true;

    }


    ArrayList<Order> getOrders() {
        ArrayList<Order> orderArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table_orders, null);
        Order order;

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String name = c.getString(c.getColumnIndex("name"));
                    String surname = c.getString(c.getColumnIndex("surname"));
                    String address = c.getString(c.getColumnIndex("address"));
                    String phone = c.getString(c.getColumnIndex("phone"));
                    String order_id = c.getString(c.getColumnIndex("order_id"));
                    int customer_id = c.getInt(c.getColumnIndex("customer_id"));
                    int bonus_card = c.getInt(c.getColumnIndex("bonus_card"));
                    String comments = c.getString(c.getColumnIndex("comments"));
                    double total_price = c.getDouble(c.getColumnIndex("total_price"));
                    order = new Order(name, surname, address, phone, bonus_card, customer_id, order_id, comments, total_price);
                    orderArrayList.add(order);
                } while (c.moveToNext());
            }
        }
        Collections.reverse(orderArrayList);
        return orderArrayList;

    }

    int getCustomer_idFromOrder(String id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT customer_id FROM " + table_orders + " WHERE order_id = " + Integer.valueOf(id), null);


        int customer_id = c.getInt(c.getColumnIndex("customer_id"));



        return customer_id;

    }

    //Find order
    String getOrder_Id(int customer_order_id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT order_id FROM " + table_orders + " WHERE customer_id = '" + customer_order_id + "'", null);


        if (c != null) {
            if (c.moveToLast()) {


                String order_id = c.getString(c.getColumnIndex("order_id"));


                return order_id;

            }
        }
        return null;

    }

    //Find Customer
    boolean customerExists(String info) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + table_customers + " WHERE customer_id = '" + info + "'";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {

            cursor.close();
            return true;
        }
        cursor.close();
        return false;


    }

    boolean orderExists() {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + table_orders;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {

            cursor.close();
            return true;
        }
        cursor.close();
        return false;


    }

    //Sync with csv
    public boolean syncItems(Item item) {

        SQLiteDatabase db = this.getWritableDatabase();
        String order_category = item.getCategory();
        String order_item = item.getItem();
        String order_price = item.getPrice();
        long result = 1;

        if (!exists(order_item)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(category, order_category);
            contentValues.put(this.item, order_item);
            contentValues.put(price, order_price);

            result = db.insert(table_items, null, contentValues);
        }
        db.close();
        if (result == -1) return false;
        return true;

    }

    public boolean exists(String customer_item) {
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            c = db.rawQuery("SELECT * FROM " + table_items + " WHERE item = '" + customer_item.trim() + "'", null);
            if (c.moveToFirst()) {
                return true;
            }
            return false;
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {

            }
        }
    }

    ArrayList<Item> getItems() {
        ArrayList<Item> itemArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table_items, null);
        Item items;

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String category = c.getString(c.getColumnIndex("category"));
                    String item = c.getString(c.getColumnIndex("item"));
                    String price = c.getString(c.getColumnIndex("price"));
                    items = new Item(category, item, price);
                    itemArrayList.add(items);
                } while (c.moveToNext());
            }
        }
        return itemArrayList;

    }


    // Adding new Order_item
    public boolean insertNewOrderItem(String customer_order_id, Item customer_item, String comment) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(order_id, customer_order_id);
        contentValues.put(category, customer_item.getCategory());
        contentValues.put(item, customer_item.getItem());
        contentValues.put(price, customer_item.getPrice());
        contentValues.put(this.comment, comment);

        long result = db.insert(table_order_items, null, contentValues);

        db.close();
        if (result == -1) return false;
        return true;
    }

    ArrayList<OrderItem> getOrderItems(String customer_order_id) {
        ArrayList<OrderItem> itemArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table_order_items + " WHERE order_id = '" + customer_order_id + "'", null);
        OrderItem items;

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    int order_item_id = c.getInt(c.getColumnIndex("order_item_id"));
                    String item = c.getString(c.getColumnIndex("item"));
                    int order_id = c.getInt(c.getColumnIndex("order_id"));
                    String category = c.getString(c.getColumnIndex("category"));
                    double price = c.getDouble(c.getColumnIndex("price"));
                    String comment = c.getString(c.getColumnIndex("comment"));
                    items = new OrderItem(order_id, item, category, price, comment, order_item_id);
                    itemArrayList.add(items);
                } while (c.moveToNext());
            }
        }

        db.close();
        return itemArrayList;

    }


    ArrayList<String> getItemCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT category FROM " + table_items, null);
        ArrayList<String> categories = new ArrayList<>();
        String temp="";
        String category = "null";
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    if (temp != category) {
                        temp = category;
                        category = c.getString(c.getColumnIndex("category"));

                        categories.add(category);
                    }
                } while (c.moveToNext());
            }
        }

        db.close();
        return categories;

    }

    ArrayList<Item> getItemsByType(String type){
        ArrayList<Item> itemArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table_items + " WHERE category = '" + type + "'", null);
        Item items;

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String category = c.getString(c.getColumnIndex("category"));
                    String item = c.getString(c.getColumnIndex("item"));
                    String price = c.getString(c.getColumnIndex("price"));
                    items = new Item(category, item, price);
                    itemArrayList.add(items);
                } while (c.moveToNext());
            }
        }
        return itemArrayList;
    }

    public boolean update_bonus_card(int new_bonus_card, Customer customer ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(bonus_card, new_bonus_card);


        long result = db.update(table_customers ,contentValues , customer_id + " = " + customer.getCustomer_id(),null);


        if (result == -1) return false;
        return true;
    }

    public boolean update_name(String new_name, Customer customer ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(name, new_name);


        long result = db.update(table_customers ,contentValues , customer_id + " = " + customer.getCustomer_id(),null);


        if (result == -1) return false;
        return true;
    }

    public boolean update_surname(String new_surname, Customer customer ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(surname, new_surname);


        long result = db.update(table_customers ,contentValues , customer_id + " = " + customer.getCustomer_id(),null);


        if (result == -1) return false;
        return true;
    }

    public boolean update_address(String new_address, Customer customer ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(address, new_address);


        long result = db.update(table_customers ,contentValues , customer_id + " = " + customer.getCustomer_id(),null);


        if (result == -1) return false;
        return true;
    }

    public boolean update_phone(String new_phone, Customer customer ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(phone, new_phone);


        long result = db.update(table_customers ,contentValues , customer_id + " = " + customer.getCustomer_id(),null);


        if (result == -1) return false;
        return true;
    }

    double total_price(ArrayList<ChosenItem> order_items, int bonus_card, int customer_id){
        int i;
        double total = 0.0;
        for (i=0;i<order_items.size();i++){
            if (order_items.get(i).getCategory().equals("καφέδες")){
                if (bonus_card >= 6){
                    bonus_card= bonus_card - 6;
                    update_bonus_card(bonus_card, getCustomer(customer_id) );

                }
                else{
                    String price = order_items.get(i).getPrice();
                    total += Double.parseDouble(price.replaceAll("[^0-9\\\\.]+", ""));
                }
            }else {
                String price = order_items.get(i).getPrice();
                total += Double.parseDouble(price.replaceAll("[^0-9\\\\.]+", ""));
            }

        }
        return total;

    }


    // sync table customers
    public boolean syncCustomers(Customer customer) {

        SQLiteDatabase db = this.getWritableDatabase();
        int customer_id = customer.getCustomer_id();
        String customer_name = customer.getName();
        String customer_surname = customer.getSurname();
        String customer_address = customer.getAddress();
        String customer_phone = customer.getPhone();
        int customer_bonus_card = customer.getBonus_card();
        long result = 1;

        if (!customerExists(String.valueOf(customer_id))) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.customer_id, customer_id);
            contentValues.put(name, customer_name);
            contentValues.put(surname, customer_surname);
            contentValues.put(address, customer_address);
            contentValues.put(phone, customer_phone);
            contentValues.put(bonus_card, customer_bonus_card);

            result = db.insert(table_customers, null, contentValues);
        }
        db.close();
        if (result == -1) return false;
        return true;

    }

    // sync table customers
    public boolean syncOrders(Order order) {

        SQLiteDatabase db = this.getWritableDatabase();
        int customer_id = order.getCustomer_id();
        String customer_name = order.getName();
        String customer_surname = order.getSurname();
        String customer_address = order.getAddress();
        String customer_phone = order.getPhone();
        int customer_bonus_card = order.getBonus_card();
        int order_id = Integer.valueOf(order.getOrder_id());
        String comments = order.getComments();
        double total_price = order.getTotal_price();

        long result = 1;

        if (!OrderIdExists(order_id)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.customer_id, customer_id);
            contentValues.put(name, customer_name);
            contentValues.put(surname, customer_surname);
            contentValues.put(address, customer_address);
            contentValues.put(phone, customer_phone);
            contentValues.put(bonus_card, customer_bonus_card);
            contentValues.put(this.order_id, order_id);
            contentValues.put(this.comments, comments);
            contentValues.put(this.total_price, total_price);

            int id = order_id-50;
            if (OrderIdExists(id)) db.delete(table_orders, this.order_id + "=" + id, null);

            result = db.insert(table_orders, null, contentValues);
        }
        db.close();
        if (result == -1) return false;
        return true;

    }


    // sync table orderitems
    public boolean syncOrderItems(OrderItem order_item) {

        SQLiteDatabase db = this.getWritableDatabase();
        int order_id = order_item.getOrder_id();
        String item = order_item.getItem();
        String category = order_item.getCategory();
        double price = order_item.getPrice();
        String comment = order_item.getComment();
        int order_item_id = order_item.getOrder_item_id();

        long result = 1;

        if (!OrderItemExists(order_item_id)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.order_id, order_id);
            contentValues.put(this.item, item);
            contentValues.put(this.category, category);
            contentValues.put(this.price, price);
            contentValues.put(this.comment, comment);
            contentValues.put(this.order_item_id, order_item_id);

            int id = order_item_id-50;
            if (OrderItemExists(id)) db.delete(table_order_items, this.order_item_id + "=" + id, null);

            result = db.insert(table_order_items, null, contentValues);
        }
        db.close();
        if (result == -1) return false;
        return true;

    }

    boolean OrderItemExists(int info) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + table_order_items + " WHERE order_item_id = '" + info + "'";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {

            cursor.close();
            return true;
        }
        cursor.close();
        return false;


    }


    boolean OrderIdExists(int info) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + table_orders + " WHERE order_id = '" + info + "'";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {

            cursor.close();
            return true;
        }
        cursor.close();
        return false;


    }

    ArrayList<Order> getCustomerOrders(Customer customer) {
        ArrayList<Order> orderArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table_orders + " WHERE customer_id = '" + customer.getCustomer_id() + "'", null);
        Order order;

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String name = c.getString(c.getColumnIndex("name"));
                    String surname = c.getString(c.getColumnIndex("surname"));
                    String address = c.getString(c.getColumnIndex("address"));
                    String phone = c.getString(c.getColumnIndex("phone"));
                    String order_id = c.getString(c.getColumnIndex("order_id"));
                    int customer_id = c.getInt(c.getColumnIndex("customer_id"));
                    int bonus_card = c.getInt(c.getColumnIndex("bonus_card"));
                    String comments = c.getString(c.getColumnIndex("comments"));
                    double total_price = c.getDouble(c.getColumnIndex("total_price"));
                    order = new Order(name, surname, address, phone, bonus_card, customer_id, order_id, comments, total_price);
                    orderArrayList.add(order);
                } while (c.moveToNext());
            }
        }
        Collections.reverse(orderArrayList);
        return orderArrayList;

    }

    boolean update_order(ArrayList<Order> CustomerOrders, Customer customer){
        int max_order_id=0;
        for (int i=0;i<CustomerOrders.size();i++){
            if (max_order_id < Integer.valueOf(CustomerOrders.get(i).order_id)) {
                max_order_id = Integer.valueOf(CustomerOrders.get(i).order_id);
            }
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(bonus_card, customer.getBonus_card());


        long result = db.update(table_orders ,contentValues , order_id + " = " + max_order_id,null);


        if (result == -1) return false;
        return true;

    }

    boolean delete_order(String id) {
        ArrayList<OrderItem> orderItems = getOrderItems(id);
        int counter=0;
        for (int i=0; i<orderItems.size(); i++)
        {
            if (orderItems.get(i).getCategory().equals("καφέδες")){
                counter++;
            }
        }
        Customer customer = getCustomer(Integer.valueOf(id));
        int customer_id_from_order = getCustomer_idFromOrder(id);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int new_bonus_card = customer.getBonus_card() - counter;
        contentValues.put(bonus_card, new_bonus_card);
        db.update(table_customers, contentValues, customer_id + " = " + customer_id_from_order, null);
        long result = db.delete(table_orders ,order_id + " = " + id,null);

        if (result == -1) return false;
        return true;
    }

    boolean delete_customer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(table_customers ,customer_id + " = " + id,null);

        if (result == -1) return false;
        return true;
    }

    void clear(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + table_customers);
        db.execSQL("DELETE FROM " + table_orders);
        db.execSQL("DELETE FROM " + table_items);
        db.execSQL("DELETE FROM " + table_order_items);
    }

}