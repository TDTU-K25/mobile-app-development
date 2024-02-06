package com.example.ex2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tvFollowingNumber;
    TextView tvFollowersNumber;
    Button btnFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvFollowersNumber = findViewById(R.id.tv_followers_number);
        tvFollowingNumber = findViewById(R.id.tv_following_number);
        btnFollow = findViewById(R.id.btn_follow);

        tvFollowingNumber.setText(String.valueOf(getRandomNum(100, 10000)));
        tvFollowersNumber.setText(String.valueOf(getRandomNum(100, 10000)));

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFollow.getText().toString().toLowerCase(Locale.ROOT).equals("follow")) {
                    tvFollowersNumber.setText(String.valueOf(Integer.parseInt(tvFollowersNumber.getText().toString()) + 1));
                    btnFollow.setText("UNFOLLOW");
                } else if(btnFollow.getText().toString().toLowerCase(Locale.ROOT).equals("unfollow")) {
                    tvFollowersNumber.setText(String.valueOf(Integer.parseInt(tvFollowersNumber.getText().toString()) - 1));
                    btnFollow.setText("FOLLOW");
                }
            }
        });
    }

    private Integer getRandomNum(int min, int max) {
        return (int)Math.floor(Math.random() * (max - min + 1) + min);
    }
}