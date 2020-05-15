package com.example.whatscookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AddIngredientActivity extends AppCompatActivity {
    Button backButton;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<Ingredient, IngredientViewHolder> adapter;
    final String INGREDIENTCOLLECTION = "Ingredients";
    final String INGREDIENTNAME = "ingredientName";
    final static String TAG = "debugTag";
    public static final String INTENTKEY = "IngredientKey";
    Context mContext = AddIngredientActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);
        backButton = findViewById(R.id.addIngredientReturnButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.addIngredientRecyclerView);
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int columns = (int)dpWidth/60;
        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));
        setUpAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void setUpAdapter(){
        FirebaseApp.initializeApp(this);
        Query query = FirebaseFirestore.getInstance().collection(INGREDIENTCOLLECTION).orderBy(INGREDIENTNAME);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        FirestoreRecyclerOptions<Ingredient> options = new FirestoreRecyclerOptions.Builder<Ingredient>()
                .setQuery(query, Ingredient.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Ingredient, IngredientViewHolder>(options) {
            @Override
            protected void onBindViewHolder(IngredientViewHolder ingredientViewHolder, int i, Ingredient ingredient) {
                Log.d(TAG, "Ingredient Name = " + ingredient.getIngredientName());
                Log.d(TAG, "Ingredient URL = "+ ingredient.getIngredientURL());
                ingredientViewHolder.setIngredientImage(ingredient.getIngredientURL());
                String ingredientName = toFirstCharUpperAll(ingredient.getIngredientName());
                ingredientViewHolder.setIngredientName(ingredientName);
                ingredientViewHolder.setTaggedIngredient(ingredient);
            }

            @NonNull
            @Override
            public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_ingredient_list_item, parent, false);
                return new IngredientViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        /*recyclerView.setLayoutManager(new GridLayoutManager(mContext, GridLayoutManager.DEFAULT_SPAN_COUNT));*/
    }
    private String toFirstCharUpperAll(String string){
        StringBuffer sb=new StringBuffer(string);
        for (int i = 0; i < sb.length(); i++)
            if(i == 0 || sb.charAt(i - 1) == ' ')
                sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
        return sb.toString();
    }

    private class IngredientViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredientNameView;
        private ImageView ingredientImageView;
        private Ingredient taggedIngredient;


        IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientNameView = itemView.findViewById(R.id.addIngredientName);
            ingredientImageView = itemView.findViewById(R.id.ingredientImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    assert (taggedIngredient != null);
                    Log.d(TAG, "Tagged ingredient has a name of " + taggedIngredient.getIngredientName());
                    Intent intent = new Intent();
                    intent.putExtra(INTENTKEY, taggedIngredient);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
        }

        void setIngredientName(String ingredientName) {
            ingredientNameView.setText(ingredientName);
        }
        void setIngredientImage(String imageURL){
            Drawable d = ContextCompat.getDrawable(mContext, R.drawable.ic_web_asset_black_40dp);
            ingredientImageView.setImageDrawable(d);
        }
        void setTaggedIngredient(Ingredient i){
            taggedIngredient = i;
        }

    }
}
