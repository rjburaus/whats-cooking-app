package com.example.whatscookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    Context mContext = MainActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        Runnable swapToListIngredientsActivity = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, ListIngredientsActivity.class);
                startActivity(intent);
            }
        };
        handler.postDelayed(swapToListIngredientsActivity, 2000);

    }
}
