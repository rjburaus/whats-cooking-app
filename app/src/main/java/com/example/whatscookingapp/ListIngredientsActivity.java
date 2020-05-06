package com.example.whatscookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ListIngredientsActivity extends AppCompatActivity {
    ArrayList<String> ingredientList;
    final String INTENTKEY = "ingredientList";
    String[] intentIngredients = new String[0];
    Context mContext = ListIngredientsActivity.this;
    Button addIngredientButton;
    Button resetButton;
    Button searchButton;
    IngredientListAdapter adapter;
    ListView listView;
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ingredients);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        resetButton = findViewById(R.id.resetButton);
        searchButton = findViewById(R.id.searchForRecipeButton);
        ingredientList = new ArrayList<String>();
        listView = findViewById(R.id.ingredientListView);
        welcomeText = findViewById(R.id.listIngredientsWelcomeText);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            intentIngredients = extras.getStringArray(INTENTKEY);
            assert intentIngredients != null;
            ingredientList.addAll(Arrays.asList(intentIngredients));
        }
        else{
            ingredientList = new ArrayList<String>();
        }
        if (ingredientList.size() > 0){
            welcomeText.setVisibility(View.GONE);
        }
        adapter = new IngredientListAdapter(mContext, ingredientList);
        listView.setAdapter(adapter);
        setOnClickListeners();


    }
    public void setOnClickListeners(){
        //Add ingredient Button using filler data
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientList.add("Banana");
                ingredientList.add("Milk");
                welcomeText.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
        //Reset Button Working
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientList.clear();
                Toast.makeText(mContext, "Ingredient list cleared", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                welcomeText.setVisibility(View.VISIBLE);
            }
        });
        //Search not yet implemented
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}
