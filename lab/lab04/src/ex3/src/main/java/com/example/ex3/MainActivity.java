package com.example.ex3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ex3.adapter.CustomAdapter;
import com.example.ex3.model.Brand;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomAdapter adapter;
    Button btnRemoveAll;
    Button btnRemoveSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_listItem);
        btnRemoveAll = findViewById(R.id.btn_removeAll);
        btnRemoveSelected = findViewById(R.id.btn_removeSelected);

        // set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // set adapter
        ArrayList<Brand> phoneBrands = new ArrayList<>();
        phoneBrands.add(new Brand("Apple"));
        phoneBrands.add(new Brand("Samsung"));
        phoneBrands.add(new Brand("Nokia"));
        phoneBrands.add(new Brand("Oppo"));

        adapter = new CustomAdapter(this, phoneBrands);
        recyclerView.setAdapter(adapter);

        btnRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeAll();
            }
        });

        btnRemoveSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeSelected();
            }
        });
    }
}