package com.example.whatscookingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchActivityAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<Recipe> recipes;
    View.OnClickListener mRowClickListener;
    List<Ingredient> ingredients;

    public SearchActivityAdapter(Context context, List<Recipe> recipeData, View.OnClickListener rowClickListener, List<Ingredient> ingredientsData)
    {
        mContext = context;
        recipes = recipeData;
        mRowClickListener = rowClickListener;
        ingredients = ingredientsData;
    }
    public void updateRecipes(List<Recipe> recipeList){
        recipes = recipeList;
    }

    @NonNull
    @Override
    //If no views available inflate one here and find views by id in viewholder constructor
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.search_listrow, null);
        return new ViewHolder(view, mRowClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder recyclerViewHolder = (ViewHolder)viewHolder;

        Recipe data = recipes.get(i);
        recyclerViewHolder.setmRecipe(data);
        recyclerViewHolder.mRecipeName.setText(toFirstCharUpperAll(data.getRecipeName()));
        recyclerViewHolder.itemView.setTag(i);
    }

    public Recipe getItem(int position)
    {
        return recipes.get(position);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mRecipeName;
        ImageView mRecipeImage;
        Recipe mRecipe;


        ViewHolder(View itemView, View.OnClickListener rowClickListener)
        {
            super(itemView);
            this.itemView.setOnClickListener(rowClickListener);
            mRecipeName = itemView.findViewById(R.id.searchRecipeNameText);
            mRecipeImage = itemView.findViewById(R.id.searchRecipeImage);

        }

        public void setmRecipe(Recipe mRecipe) {
            this.mRecipe = mRecipe;
        }
    }
    private String toFirstCharUpperAll(String string){
        StringBuffer sb=new StringBuffer(string);
        for (int i = 0; i < sb.length(); i++)
            if(i == 0 || sb.charAt(i - 1) == ' ')
                sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
        return sb.toString();
    }
}
