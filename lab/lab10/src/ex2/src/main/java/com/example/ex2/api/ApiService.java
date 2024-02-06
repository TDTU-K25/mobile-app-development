package com.example.ex2.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiService {
    public ArrayList<String> getAllCountries() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://covid-193.p.rapidapi.com/countries").get().addHeader("X-RapidAPI-Key", "697ccea590mshc51fd9e3d14decdp100e8ajsncb9c5b8250f8").addHeader("X-RapidAPI-Host", "covid-193.p.rapidapi.com").build();
        Response response = client.newCall(request).execute();
        String responseData = response.body().string(); // JSON
        JSONObject json = new JSONObject(responseData); // convert JSON to Object
        JSONArray countries = (JSONArray) json.get("response");
        ArrayList<String> result = new ArrayList<String>();
        if (countries != null) {
            for (int i = 0; i < countries.length(); i++) {
                result.add(countries.getString(i));
            }
        }
        return result;
    }

    public JSONObject getStatistics(String country) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        if (country == null) {
            request = new Request.Builder().url("https://covid-193.p.rapidapi.com/statistics").get().addHeader("X-RapidAPI-Key", "697ccea590mshc51fd9e3d14decdp100e8ajsncb9c5b8250f8").addHeader("X-RapidAPI-Host", "covid-193.p.rapidapi.com").build();
        } else {
            request = new Request.Builder().url("https://covid-193.p.rapidapi.com/statistics/?country=" + country).get().addHeader("X-RapidAPI-Key", "697ccea590mshc51fd9e3d14decdp100e8ajsncb9c5b8250f8").addHeader("X-RapidAPI-Host", "covid-193.p.rapidapi.com").build();
        }
        Response response = client.newCall(request).execute();
        String responseData = response.body().string(); // JSON
        JSONObject json = new JSONObject(responseData); // convert JSON to Object
        JSONArray statistics = (JSONArray) json.get("response");
        JSONObject statInfo = null;
        if (country == null) { // all
            statInfo = find(statistics, "All");
        } else { // specific country
            statInfo = (JSONObject) statistics.get(0);
        }
        return statInfo;
    }

    private JSONObject find(JSONArray array, String keyword) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject result = null;
            try {
                result = array.getJSONObject(i);
                if (result.getString("country").equals(keyword)) {
                    return result;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
