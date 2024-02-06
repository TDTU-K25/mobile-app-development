package com.example.ex2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ex2.api.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView tvCountry;
    TextView tvNumActiveCases;
    TextView tvNumRecoveredCases;
    TextView tvNumCriticalCases;
    TextView tvNumDeathCases;
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        getStatInfo(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_management, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.choose_country) {
            Intent intent = new Intent(this, CountryActivity.class);
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String countryName = data.getStringExtra("countryName");
            getStatInfo(countryName);
        }
    }

    private void getStatInfo(String countryName) {
        ApiService apiService = new ApiService();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject statInfo = apiService.getStatistics(countryName);
                    while (statInfo == null) ; // wait for response then update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tvCountry.setText(statInfo.getString("country"));
                                JSONObject cases = (JSONObject) statInfo.get("cases");
                                if (cases.isNull("active")) cases.put("active", 0);
                                if (cases.isNull("recovered")) cases.put("recovered", 0);
                                if (cases.isNull("critical")) cases.put("critical", 0);
                                tvNumActiveCases.setText(String.valueOf(cases.getInt("active")));
                                tvNumRecoveredCases.setText(String.valueOf(cases.getInt("recovered")));
                                tvNumCriticalCases.setText(String.valueOf(cases.getInt("critical")));
                                JSONObject deaths = (JSONObject) statInfo.get("deaths");
                                if (deaths.isNull("total")) deaths.put("total", 0);
                                tvNumDeathCases.setText(String.valueOf(deaths.getInt("total")));
                                tvDate.setText(statInfo.getString("time"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void initViews() {
        tvCountry = findViewById(R.id.tv_country);
        tvNumActiveCases = findViewById(R.id.tv_num_active_cases);
        tvNumRecoveredCases = findViewById(R.id.tv_num_recovered_cases);
        tvNumCriticalCases = findViewById(R.id.tv_num_critical_cases);
        tvNumDeathCases = findViewById(R.id.tv_num_death_cases);
        tvDate = findViewById(R.id.tv_date);
    }
}