package com.xiangxun.sampling.fun;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiangxun.sampling.R;


public class FunctionListAdapter extends BaseAdapter {
    private Function[] functions;
    private Context context;
    private ListView mListView;
    private int size = 0;
    private boolean isShow = false;

    public FunctionListAdapter(Context context, ListView mListView) {
        this.context = context;
        this.mListView = mListView;
    }

    public void setData(Function[] itemList) {
        if (itemList != null) {
            this.functions = itemList;
            size = itemList.length;
        }

        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        if (functions != null) {
            return functions[position];
        }
        return null;
    }

    public void setShowArrow(boolean value) {
        isShow = value;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.functionlist_item, null);
        }

        ImageView image = (ImageView) view.findViewById(R.id.function_image);
        TextView name = (TextView) view.findViewById(R.id.function_name);
        TextView note = (TextView) view.findViewById(R.id.function_note);
        ImageView arrow = (ImageView) view.findViewById(R.id.function_arrow);

        Function function = functions[position];
        image.setImageResource(function.getIconId());
        name.setText(function.getTitleId());

        if (function.getDescription() != null) {
            if (function.getDescription().contains("New!")) {
                note.setText(function.getDescription());
                note.setTextColor(Color.RED);
            } else {
                note.setText(function.getDescription());
                note.setTextColor(context.getResources().getColor(R.color.color999999));
            }
        }
        if (isShow)
            arrow.setVisibility(View.VISIBLE);
        else
            arrow.setVisibility(View.GONE);

        return view;
    }
}
