package com.example.whatscookingapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IngredientListAdapter extends BaseAdapter {
    List<String> ingredients = new ArrayList<String>();
    Context mContext;
    public IngredientListAdapter(Context context, ArrayList<String> list)
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
        viewHolder.ingredientName.setText((String)getItem(i));
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
}

