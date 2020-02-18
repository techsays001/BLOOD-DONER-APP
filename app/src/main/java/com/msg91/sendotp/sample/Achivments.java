
package com.msg91.sendotp.sample;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msg91.sendotp.sample.ui.tools.ToolsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Achivments extends AppCompatActivity {

SharedPreferences sh;
    List<Cheque3> productList;
    SwipeRefreshLayout s;
    //the recyclerview
    RecyclerView recyclerView;
    SwipeRefreshLayout swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achivments);

        sh=getSharedPreferences("data",MODE_PRIVATE);

        recyclerView = findViewById(R.id.recylcerViewcqown);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipe=findViewById(R.id.swiperefreshqown);

        //    Toast.makeText(getApplicationContext(), j.getStringExtra("ph"), Toast.LENGTH_LONG).show();
        loadProducts();
        //initializing the productlist
        productList = new ArrayList<>();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productList.clear();
                loadProducts();
            }
        });

    }

    private void loadProducts() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://androidprojectstechsays.000webhostapp.com/Blood_Doner_App/own_req.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        swipe.setRefreshing(false);

                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = array.length()-1; i >=0; i--) {


                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                //adding the product to product list
                                productList.add(new Cheque3(
                                        product.getString("name"),
                                        product.getString("blood"),
                                        product.getString("date"),
                                        product.getString("city")


                                ));
                            }


                            Chequeadapter3 adapter = new Chequeadapter3(Achivments.this, productList);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

               params.put("ph",sh.getString("phone",null));

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(Achivments.this);
        requestQueue.add(stringRequest);
    }
}

