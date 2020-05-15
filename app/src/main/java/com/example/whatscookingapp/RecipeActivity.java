package com.example.whatscookingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class RecipeActivity extends AppCompatActivity {

    Recipe recipe;
    ImageView imageView;
    TextView recipeNameTextView;
    TextView ingredientsTextView;
    TextView instructionsTextView;
    Button backButton;
    Context mContext = RecipeActivity.this;


    public String displayIngredients() {
        String result = "";

        for (int i = 0; i < recipe.getRecipeIngredients().size(); i++) {
            String ingredient = recipe.getRecipeIngredients().get(String.valueOf(i));
            result += ingredient + "\r\n";
        }

        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        recipe = (Recipe) getIntent().getSerializableExtra(SearchActivity.INTENTTAG);
        imageView = (ImageView) findViewById(R.id.imageView);
        recipeNameTextView = (TextView) findViewById(R.id.recipeNameTextView);
        ingredientsTextView = (TextView) findViewById(R.id.ingredientsTextView);
        instructionsTextView = (TextView) findViewById(R.id.instructionsTextView);
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageDownloader task = new ImageDownloader();

        try {
            Bitmap bitmap = task.execute(recipe.getRecipeURL()).get();
            imageView.setImageBitmap(bitmap);

        } catch (ExecutionException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recipeNameTextView.setText(recipe.getRecipeName());
        ingredientsTextView.setText(displayIngredients());
        instructionsTextView.setMovementMethod(new ScrollingMovementMethod());
        instructionsTextView.setText(recipe.getRecipeInstruction());
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}