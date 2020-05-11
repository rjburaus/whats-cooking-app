package com.example.whatscookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Ingredient> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ingredients = (ArrayList<Ingredient>) getIntent().getSerializableExtra(ListIngredientsActivity.INTENTKEY);
        assert (ingredients != null);

    }
}
