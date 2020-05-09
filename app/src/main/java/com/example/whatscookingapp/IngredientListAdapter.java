package com.example.whatscookingapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IngredientListAdapter extends BaseAdapter {
    List<Ingredient> ingredients = new ArrayList<Ingredient>();
    Context mContext;
    public IngredientListAdapter(Context context, ArrayList<Ingredient> list)
    {
        mContext = context;
        ingredients = list;
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public Object getItem(int i) {
        return ingredients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item, null);
            TextView inum = convertView.findViewById(R.id.IngredientNumber);
            TextView iname = convertView.findViewById(R.id.IngredientName);

            ViewHolder viewHolder = new ViewHolder(inum, iname);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        int j = i+1;

        viewHolder.ingredientNumber.setText(j + ": ");
        Ingredient ingredient = (Ingredient)getItem(i);
        viewHolder.ingredientName.setText(toFirstCharUpperAll(ingredient.getIngredientName()));
        return convertView;
    }
    public static class ViewHolder
    {
        TextView ingredientNumber;
        TextView ingredientName;

        ViewHolder(TextView number, TextView name)
        {
            ingredientNumber = number;
            ingredientName = name;
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

