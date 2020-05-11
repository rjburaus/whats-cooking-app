package com.example.whatscookingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
    final static String INTENTKEY = "ingredientList";
    ArrayList<Ingredient> ingredientArrayList = new ArrayList<Ingredient>();
    Context mContext = ListIngredientsActivity.this;
    Button addIngredientButton;
    Button resetButton;
    Button searchButton;
    IngredientListAdapter adapter;
    ListView listView;
    TextView welcomeText;
    final int CHILDACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ingredients);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        resetButton = findViewById(R.id.resetButton);
        searchButton = findViewById(R.id.searchForRecipeButton);
        listView = findViewById(R.id.ingredientListView);
        welcomeText = findViewById(R.id.listIngredientsWelcomeText);

        if (ingredientArrayList.size() > 0){
            welcomeText.setVisibility(View.GONE);
        }
        adapter = new IngredientListAdapter(mContext, ingredientArrayList);
        listView.setAdapter(adapter);
        setOnClickListeners();


    }
    public void setOnClickListeners(){
        //Add ingredient Button using filler data
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ingredientList.add("Banana");
                ingredientList.add("Milk");
                welcomeText.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();*/
                Intent intent = new Intent(mContext, AddIngredientActivity.class);
                startActivityForResult(intent, CHILDACTIVITY);
            }
        });
        //Reset Button Working
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientArrayList.clear();
                Toast.makeText(mContext, "Ingredient list cleared", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                welcomeText.setVisibility(View.VISIBLE);
            }
        });
        //Search not yet implemented
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra(INTENTKEY, ingredientArrayList);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHILDACTIVITY && resultCode == Activity.RESULT_OK){
            assert (data != null);
            Ingredient i = (Ingredient)data.getSerializableExtra(AddIngredientActivity.INTENTKEY);
            ingredientArrayList.add(i);
            adapter.notifyDataSetChanged();
        }
    }
}
