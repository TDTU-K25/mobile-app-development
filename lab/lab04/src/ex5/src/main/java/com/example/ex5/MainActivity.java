package com.example.ex5;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex5.adapter.CustomAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomAdapter adapter;
    Button btnAdd;
    Button btnRemove;
    TextView tvQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_users);
        btnAdd = findViewById(R.id.btn_add);
        btnRemove = findViewById(R.id.btn_remove);
        tvQuantity = findViewById(R.id.tv_quantity);

        // set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // set adapter
        adapter = new CustomAdapter(this);
        recyclerView.setAdapter(adapter);

        updateUIQuantity(adapter.getItemCount());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addFiveUsers();
                updateUIQuantity(adapter.getItemCount());
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeLastFiveUsers();
                updateUIQuantity(adapter.getItemCount());
            }
        });
    }

    private void updateUIQuantity(Integer quantity) {
        tvQuantity.setText(String.format("Total users: %d", quantity));
    }
}