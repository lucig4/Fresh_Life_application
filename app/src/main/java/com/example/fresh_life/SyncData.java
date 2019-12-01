package com.example.fresh_life;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SyncData {
    Context context;
    ArrayList<Item> Item_List;
    ArrayList<Customer> customer_list;
    ArrayList<Order> order_list;
    ArrayList<OrderItem> order_items_list;
    DatabaseHelper db;
    RequestQueue queue;


    SyncData(Context context){
        this.context=context;

    }

    void sync(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                /*
                 * Do something
                 */


                db = new DatabaseHelper(context);
                db.clear();

                queue = Volley.newRequestQueue(context);
                getitemsfromserver();



                getcustomersfromserver();



                getOrdersfromserver();

                getOrderItemsfromserver();

            }
        });
        t.start();

    }

    void save(){


        Thread t = new Thread(new Runnable() {
            public void run() {
                /*
                 * Do something
                 */

                queue = Volley.newRequestQueue(context);
                db = new DatabaseHelper(context);
                ArrayList<Customer> customers_array = db.getCustomers();

                for (int i=0;i<customers_array.size();i++) {
                    registerUser(customers_array.get(i));
                }

                ArrayList<Order> orders_array = db.getOrders();
                ArrayList<OrderItem> order_items;
                for (int i=0;i<orders_array.size();i++) {
                    uploadOrders(orders_array.get(i));
                    order_items = db.getOrderItems(orders_array.get(i).getOrder_id());
                    for (int y=0;y<order_items.size();y++){
                        uploadOrderItems(order_items.get(y));
                    }
                }


            }

        });
        t.start();




    }



    private void getitemsfromserver() {
        Item_List = new ArrayList<>();
        db = new DatabaseHelper(context) ;
        String url = "https://fresh-life//////////////////////////";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                Item item = new Item(
                                        product.getString("type"),
                                        product.getString("item"),
                                        product.getString("price")
                                );
                                Item_List.add(item);
                                if (db.syncItems(Item_List.get(i))){


                                }else{

                                    String text= "error getting items";

                                    int duration = Toast.LENGTH_LONG;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            String text= "error " + e.getMessage();

                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(stringRequest);

    }








    private void registerUser(final Customer customer) {
        String url = "https://fresh-life//////////////////////////";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(context, error.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", String.valueOf(customer.getCustomer_id()));
                params.put("name", customer.getName());
                params.put("surname", customer.getSurname());
                params.put("address", customer.getAddress());
                params.put("phone", customer.getPhone());
                params.put("bonus_card", String.valueOf(customer.getBonus_card()));
                //params.put("bonus_card", customer.getBonus_card());


                return params;
            }


        };
        // Access the RequestQueue through your singleton class.
        queue.add(stringRequest);
    }


    // java customer download
    private void getcustomersfromserver() {
        customer_list = new ArrayList<>();
        db = new DatabaseHelper(context) ;
        String url = "https://fresh-life//////////////////////////";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                Customer customer = new Customer(
                                        product.getString("name"),
                                        product.getString("surname"),
                                        product.getString("address"),
                                        product.getString("phone"),
                                        Integer.parseInt(product.getString("bonus_card")),
                                        Integer.parseInt(product.getString("customer_id"))
                                );
                                customer_list.add(customer);
                                if (db.syncCustomers(customer_list.get(i))){


                                }else{

                                    String text= "error getting customers";

                                    int duration = Toast.LENGTH_LONG;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            String text= "error";

                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(stringRequest);

    }


    private void uploadOrders(final Order order) {
        String url = "https://fresh-life//////////////////////////";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(context, error.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", String.valueOf(order.getCustomer_id()));
                params.put("name", order.getName());
                params.put("surname", order.getSurname());
                params.put("address", order.getAddress());
                params.put("phone", order.getPhone());
                params.put("bonus_card", String.valueOf(order.getBonus_card()));
                params.put("order_id", String.valueOf(order.getOrder_id()));
                params.put("comments", order.getComments());
                params.put("total_price", String.valueOf(order.getTotal_price()));
                //params.put("bonus_card", customer.getBonus_card());


                return params;
            }


        };
        // Access the RequestQueue through your singleton class.
        queue.add(stringRequest);
    }

    // java orders download
    private void getOrdersfromserver() {
        order_list = new ArrayList<>();
        db = new DatabaseHelper(context) ;
        String url = "https://fresh-life//////////////////////////";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            String txt= "Παρακαλώ Περιμένετε";



                            Toast tst = Toast.makeText(context, txt, Toast.LENGTH_LONG);
                            tst.show();

                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                Order order = new Order(
                                        product.getString("name"),
                                        product.getString("surname"),
                                        product.getString("address"),
                                        product.getString("phone"),
                                        Integer.parseInt(product.getString("bonus_card")),
                                        Integer.parseInt(product.getString("customer_id")),
                                        product.getString("order_id"),
                                        product.getString("comments"),
                                        Double.parseDouble(product.getString("total_price"))
                                );
                                order_list.add(order);
                                if (db.syncOrders(order_list.get(i))){


                                }else{

                                    String text= "error getting orders";

                                    int duration = Toast.LENGTH_LONG;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            String text= "error in orders";

                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(stringRequest);

    }

    private void uploadOrderItems(final OrderItem order_item) {
        String url = "https://fresh-life//////////////////////////";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(context, error.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("order_id", String.valueOf(order_item.getOrder_id()));
                params.put("item", order_item.getItem());
                params.put("category", order_item.getCategory());
                params.put("comment", order_item.getComment());
                params.put("price", String.valueOf(order_item.getPrice()));
                params.put("order_item_id", String.valueOf(order_item.getOrder_item_id()));


                return params;
            }


        };
        // Access the RequestQueue through your singleton class.
        queue.add(stringRequest);
    }


    // java orderitems download
    private void getOrderItemsfromserver() {
        order_items_list = new ArrayList<>();
        db = new DatabaseHelper(context) ;
        String url = "https://fresh-life//////////////////////////";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                OrderItem order_item = new OrderItem(

                                        Integer.parseInt(product.getString("order_id")),
                                        product.getString("item"),
                                        product.getString("category"),
                                        Double.parseDouble(product.getString("price")),
                                        product.getString("comment"),
                                        Integer.parseInt(product.getString("order_item_id"))
                                );
                                order_items_list.add(order_item);
                                if (db.syncOrderItems(order_items_list.get(i))){


                                }else{

                                    String text= "error getting orders";

                                    int duration = Toast.LENGTH_LONG;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            String text= "error in order_items";

                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(stringRequest);

    }
}

