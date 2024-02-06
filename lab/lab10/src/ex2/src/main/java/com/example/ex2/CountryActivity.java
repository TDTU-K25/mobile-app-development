package com.example.ex2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex2.adapter.CountryAdapter;
import com.example.ex2.api.ApiService;

import java.util.ArrayList;

public class CountryActivity extends AppCompatActivity implements CountryAdapter.OnCountryClickListener {
    RecyclerView rvCountry;
    CountryAdapter countryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        rvCountry = findViewById(R.id.rv_country);

        ApiService apiService = new ApiService();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<String> countries = apiService.getAllCountries();
                    while (countries == null) ; // wait for response then update UI
                    countryAdapter = new CountryAdapter(countries, CountryActivity.this, CountryActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rvCountry.setAdapter(countryAdapter);
                            rvCountry.setLayoutManager(new LinearLayoutManager(CountryActivity.this, RecyclerView.VERTICAL, false));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onCountryClick(String countryName) {
        Intent replyIntent = new Intent();
        replyIntent.putExtra("countryName", countryName);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}