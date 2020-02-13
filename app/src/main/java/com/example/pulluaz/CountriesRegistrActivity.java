/*
 * Created by Rufat Asadzade on 11.02.20 15:25
 * Copyright (c) 2020. Rufat Asadzade. All rights reserved.
 */


package com.example.pulluaz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountriesRegistrActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String LOG_TAG = "onEmptyResponse";

    ProgressDialog progressDoalog;
    private Spinner spinnerCountry;
    private Spinner spinnerCity;
    private Spinner sectorSpinner;


    Button btnEndReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries_registr);

        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(500);
        progressDoalog.setMessage("Yuklenir...");
        progressDoalog.setTitle("Sonuncu merhele");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();


        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerCity = findViewById(R.id.spinnerCity);
        sectorSpinner = findViewById(R.id.sector);
        init();
        Log.d(LOG_TAG, "onCreate: ");


        ArrayList<String> arraySector = new ArrayList<>();
        arraySector.add(0, "Sectoru secin");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerSector,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectorSpinner.setAdapter(adapter);

        sectorSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {




        if (adapterView.getItemAtPosition(0).equals("Sectoru secin")){

        }
        else {
            String item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText( adapterView.getContext(),""+ item, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void init() {

       ;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://13.92.237.16/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpinnerRetrofit api = retrofit.create(SpinnerRetrofit.class);

        Call<List<Countries>> call = api.getCountries();
        call.enqueue(new Callback<List<Countries>>() {
            @Override
            public void onResponse(Call<List<Countries>> call, Response<List<Countries>> response) {
                progressDoalog.dismiss();
                Log.d(LOG_TAG, "onResponse: " + response.code());
                Log.d(LOG_TAG, "onResponse: " + response.toString());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("onSuccess", response.body().toString());

                        bindCountries(response.body());




                    } else {
                        Log.d("onEmptyResponse", "Returned empty response");
                        Toast.makeText(getApplicationContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Countries>> call, Throwable t) {
                progressDoalog.dismiss();
            }
        });
    }

    public void bindCountries(final List<Countries> countries) {
        final ArrayList<String> countriesNames = new ArrayList<>();
        countriesNames.add(0,"Olkeni Secin");
        for (int i = 0; i < countries.size(); i++) {

            countriesNames.add(countries.get(i).getName().toString());
        }

        Log.d(LOG_TAG, "bind: " + countriesNames);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, countriesNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerCountry.setAdapter(spinnerArrayAdapter);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    spinnerCity.setVisibility(View.VISIBLE);
                }else spinnerCity.setVisibility(View.GONE);


                if (adapterView.getItemAtPosition(i).equals("Olkeni Secin")){

                }
                else {
                    String item = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText( adapterView.getContext(),""+ item, Toast.LENGTH_SHORT).show();
                    spinnerCity.setVisibility(View.VISIBLE);

                }

              Countries country = countries.get(0);
              onChooseCountry(country);




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(CountriesRegistrActivity.this, "error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void onChooseCountry(Countries country) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://13.92.237.16/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpinnerRetrofit api = retrofit.create(SpinnerRetrofit.class);

        Call<List<City>> call = api.getCities(country.getId());
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                Log.d(LOG_TAG, "onResponse: " + response.code());
                Log.d(LOG_TAG, "onResponse: " + response.toString());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("onSuccess", response.body().toString());

                        bindCities(response.body());

                    } else {
                        Log.d("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {

            }
        });
    }

    public void bindCities(final List<City> cities) {
        final ArrayList<String> citiesNames = new ArrayList<>();
        citiesNames.add(0,"Seheri secin");
        for (int i = 0; i < cities.size(); i++) {

            citiesNames.add(cities.get(i).getName());
        }

        Log.d(LOG_TAG, "bind: " + citiesNames);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, citiesNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerCity.setAdapter(spinnerArrayAdapter);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Seheri Secin")){
                    String item = adapterView.getItemAtPosition(i).toString();
                    Intent intent = new Intent(getApplicationContext(),LastRegistrationActivity.class);
                    intent.putExtra("getCountry",item);
                    startService(intent);


                }
                else {
                    String item = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText( adapterView.getContext(),""+ item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(CountriesRegistrActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void EndReg(View view) {

        Intent intent = new Intent(getApplicationContext(),LastRegistrationActivity.class);
        startActivity(intent);

    }
}
