package com.msg91.sendotp.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Registration extends  AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] genderr = { "Male", "Female","Other"};
    String[] bloodd= { "A+", "A-","B+","B-","AB+","AB-","O+","O-"};
    private static int RESULT_LOAD_IMAGE = 1;
    private Bitmap bitmap;
    private Uri filePath;
    ImageView imgview;
    EditText name, email,addres,pass,cpass;
    Button update;
    Spinner blood,gender;
    CheckBox pchek,cpchek;
    SharedPreferences sh,shh;
    Location location;
    String address, city, state, country, postalCode, knownName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        imgview = findViewById(R.id.bimg);
        name = findViewById(R.id.bname);
        cpass = findViewById(R.id.bcpass);
        email = findViewById(R.id.bemail);
        pass = findViewById(R.id.bpass);
        blood = findViewById(R.id.bblood);
        gender = findViewById(R.id.bgender);
        update = findViewById(R.id.bbt);
        addres = findViewById(R.id.baddress);
        cpchek = findViewById(R.id.checkBoxcp);
        pchek = findViewById(R.id.checkBoxp);

        ArrayAdapter ap = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bloodd);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        blood.setAdapter(ap);



        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,genderr);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        gender.setAdapter(aa);
        shh = getSharedPreferences("reg", MODE_PRIVATE);

        // Toast.makeText(registeration.this, shh.getString("ph", null), Toast.LENGTH_LONG).show();

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }


//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent,IMAGE_PICK_CODE);

        });

        pchek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {

                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pchek.setText("Hide");
                }
                else
                {

                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pchek.setText("Show");
                }
            }
        });

        cpchek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {

                    cpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    cpchek.setText("Hide");
                }
                else
                {

                    cpass .setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cpchek.setText("Show");
                }
            }
        });







        sh = getSharedPreferences("loc", MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();


        LocationManager locationManager = (LocationManager) Registration.this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(Registration.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, new Registration.Listener());
        // Have another for GPS provider just in case.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new Registration.Listener());
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

    }


    private void handleLatLng(final double latitude, final double longitude) {
        Log.v("TAG", "(" + latitude + "," + longitude + ")");
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(Registration.this, Locale.getDefault());

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

        addres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addres.setText(address);
            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (name.getText().toString().isEmpty()) {

                    name.setError("Empty Field");
                } else if (email.getText().toString().isEmpty()) {

                    email.setError("Empty Field");
                } else if (addres.getText().toString().isEmpty()) {
                    addres.setError("Empty Field");
                } else if (pass.getText().toString().isEmpty()) {
                    pass.setError("Empty Field");

                } else if (cpass.getText().toString().isEmpty()) {
                    cpass.setError("Empty Field");

                } else if (pass.getText().toString().length() <= 6) {

                    pass.setError("Password Must Contain 7 Digits");
                } else if (cpass.getText().toString().length() <= 6) {

                    cpass.setError("Password Must Contain 7 Digits");
                } else if (!(pass.getText().toString().equals(cpass.getText().toString()))) {

                    Toast.makeText(Registration.this, "Password not match", Toast.LENGTH_LONG).show();

                } else {

                    class UploadImage extends AsyncTask<Bitmap, Void, String> {

                        ProgressDialog loading;
                        RequestHandler rh = new RequestHandler();

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ProgressDialog.show(Registration.this, "Uploading...", null, true, false);
                        }


                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            loading.dismiss();
                          //  Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                            if(s.equals("success"))
                            {

                                new SweetAlertDialog(Registration.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Registration Success")
                                        .setContentText("Back To Login!")
                                        .setConfirmText("Yes,Login")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog
                                                        .setTitleText("Logining...!")

                                                        .setConfirmText("OK")

                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                Intent in=new Intent(Registration.this,Signin.class);
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

                        @SuppressLint("WrongThread")
                        @Override
                        protected String doInBackground(Bitmap... params) {
                            bitmap = params[0];
                            String uploadImage = getStringImage(bitmap);

                            HashMap<String, String> data = new HashMap<>();

                            data.put("name",name.getText().toString());
                            data.put("email",email.getText().toString());
                            data.put("phone",shh.getString("ph",null));
                            data.put("blood",blood.getSelectedItem().toString());
                            data.put("add",addres.getText().toString());
                            data.put("g",gender.getSelectedItem().toString());
                            data.put("pass", cpass.getText().toString());
                            //    data.put("city", city );
//                    data.put("lo", sh.getString("lo",null));
                            data.put("img",uploadImage);
                            String result = rh.sendPostRequest("https://androidprojectstechsays.000webhostapp.com/Blood_Doner_App/User_registration.php", data);

                            return result;
                        }
                    }
                    UploadImage ui = new UploadImage();
                    ui.execute(bitmap);
                }
            }

        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgview.setImageBitmap(bitmap);
                getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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




