package com.msg91.sendotp.sample.ui.slideshow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msg91.sendotp.sample.Homepage;
import com.msg91.sendotp.sample.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;

public class SlideshowFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {


    String[] bloodd= { "A+", "A-","B+","B-","AB+","AB-","O+","O-"};
    EditText name,phone,cityy;
    Spinner blood;
    Button apply;
    SharedPreferences sh,shh;
    Location location;
    String address, city, state, country, postalCode, knownName;
    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        shh=getActivity().getSharedPreferences("data",MODE_PRIVATE);
        name=root.findViewById(R.id.nname);
        phone=root.findViewById(R.id.nph);
        cityy=root.findViewById(R.id.ncity);
        blood=root.findViewById(R.id.nblood);
        apply=root.findViewById(R.id.nbt);


        ArrayAdapter ap = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,bloodd);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        blood.setAdapter(ap);

        sh = getActivity().getSharedPreferences("loc1", MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();


        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, new SlideshowFragment.Listener());
        // Have another for GPS provider just in case.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new SlideshowFragment.Listener());
        // Try to request the location immediately
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location != null) {
            handleLatLng(location.getLatitude(), location.getLongitude());
            ed.putString("la", String.valueOf(location.getLatitude()));
            ed.putString("lo", String.valueOf(location.getLongitude()));
            ed.apply();

        }


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://androidprojectstechsays.000webhostapp.com/Blood_Doner_App/Add_blood_request.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//If we are getting success from server
                                //  Toast.makeText(regtwo.this,response,Toast.LENGTH_LONG).show();
                                if (response.contains("Registration Successful")) {

                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Applyed")
                                            .setContentText("Back to Dashboard!")
                                            .setConfirmText("Yes")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog
                                                            .setTitleText("Logining...!")

                                                            .setConfirmText("OK")

                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    Intent in = new Intent(getActivity(), Homepage.class);
                                                                    startActivity(in);
                                                                }
                                                            })
                                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                }
                                            })
                                            .show();


//
                                }


                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//You can handle error here if you want
                            }

                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();


                        params.put("name", name.getText().toString());
                        params.put("apph", shh.getString("phone", null));
                        params.put("phone", phone.getText().toString());
                        params.put("blood", blood.getSelectedItem().toString());
                        params.put("city", cityy.getText().toString());

// Toast.makeText(MainActivity.this,"submitted",Toast.LENGTH_LONG).show();

//returning parameter
                        return params;
                    }

                };

// m = Integer.parseInt(ba) - Integer.parseInt(result.getContents());
// balance.setText(m+"");


//Adding the string request to the queue
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);




            }
        });
        return root;
    }


    private void handleLatLng(final double latitude, final double longitude) {
        Log.v("TAG", "(" + latitude + "," + longitude + ")");
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getLocality();
        state = addresses.get(0).getAdminArea();
        country = addresses.get(0).getCountryName();
        postalCode = addresses.get(0).getPostalCode();
        knownName = addresses.get(0).getFeatureName();

        cityy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityy.setText(city);
            }
        });





    }
    class Listener implements LocationListener {
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            handleLatLng(latitude, longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        Toast.makeText(getApplicationContext(),dance[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

}

