package com.example.whatscookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    ArrayList<String> stringsToSearch;
    ArrayList<Ingredient> ingredients;
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    SearchActivityAdapter mAdapter;
    Context mContext;
    RecyclerView mRecyclerView;

    final String RECIPESCOLLECTION = "Recipes";
    final String RECIPEINGREDIENTS = "recipeIngredients";
    final static String INTENTTAG = "selectedRecipe";
    final static String TAG = "searchDebugTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SearchActivity.this;
        setContentView(R.layout.activity_search);
        mRecyclerView = findViewById(R.id.searchRecyclerView);

        ingredients = (ArrayList<Ingredient>) getIntent().getSerializableExtra(ListIngredientsActivity.INTENTKEY);
        assert (ingredients != null);
        for (int i = 0; i <ingredients.size(); i++){
            Log.d(TAG, "Ingredient " + i + " is " + ingredients.get(i).getIngredientName());
        }
        mAdapter = new SearchActivityAdapter(mContext, recipes, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRowClicked(view);
            }
        }, ingredients);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(mRecyclerView.getContext(), manager.getOrientation());
        mRecyclerView.addItemDecoration(itemDecor);
        mRecyclerView.setAdapter(mAdapter);
        populateStringsToSearch();
        populateRecipesArray();
    }

    private void populateStringsToSearch() {
        Set<String> stringSet = new HashSet<String>();
        for (int i = 0; i < ingredients.size(); i++){
            Ingredient currentIngredient = ingredients.get(i);
            stringSet.addAll(currentIngredient.getDependentRecipes());
        }
        stringsToSearch = new ArrayList<String>(stringSet);
        for (int i = 0; i < stringsToSearch.size(); i++) {
            Log.d(TAG, "Index " + i + " => " + stringsToSearch.get(i));
        }
    }
    private void populateRecipesArray(){
        for (int i = 0; i < stringsToSearch.size(); i++){
            Query query = FirebaseFirestore.getInstance().collection(RECIPESCOLLECTION)
                    .whereEqualTo(FieldPath.documentId(), stringsToSearch.get(i));
            query.get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.isEmpty()){
                        Log.d(TAG, "Query " + queryDocumentSnapshots.getQuery().toString() + "returned empty");
                    }
                    else {
                        ArrayList<DocumentSnapshot> snapshots = new ArrayList<DocumentSnapshot>(queryDocumentSnapshots.getDocuments());
                        for (DocumentSnapshot snapshot: snapshots){
                            Recipe r = snapshot.toObject(Recipe.class);
                            Log.d(TAG, "Query " + queryDocumentSnapshots.getQuery().toString() + " returned " + r.getRecipeName());
                            recipes.add(r);
                            Log.d(TAG, "Recipes size is " + recipes.size());
                        }
                        for (int i = 0; i < recipes.size(); i++){
                            Log.d(TAG, "Recipe " + i + " is " + recipes.get(i).getRecipeName());
                        }
                        doOnceRecipesIsUpdated();
                    }
                }
            });
        }
    }
    private void doOnceRecipesIsUpdated(){
        mAdapter.updateRecipes(recipes);
        mAdapter.notifyDataSetChanged();
    }
    private void onRowClicked(View v)
    {
        Intent intent = new Intent(mContext, RecipeActivity.class);
        int itemPosition = mRecyclerView.getChildAdapterPosition(v);
        Recipe selectedRecipe = mAdapter.getItem(itemPosition);
        intent.putExtra(INTENTTAG, selectedRecipe);
        startActivity(intent);
    }
}
