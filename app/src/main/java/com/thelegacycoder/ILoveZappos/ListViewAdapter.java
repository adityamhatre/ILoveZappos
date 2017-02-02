package com.thelegacycoder.ILoveZappos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Aditya on 002, 2 Feb, 2017.
 */

public class ListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<ResultsItem> resultsItemList;

    public ListViewAdapter(Context context, List<ResultsItem> resultsItemList) {
        this.context = context;
        this.resultsItemList = resultsItemList;
    }

    @Override
    public int getCount() {
        return resultsItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return resultsItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            //convertView = inflater.inflate(R.layout.product_item, null);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            viewHolder = new ViewHolder();
            viewHolder.productName = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ResultsItem resultsItem = (ResultsItem) getItem(i);
        viewHolder.productName.setText(resultsItem.getProductName());


        return convertView;
    }


    static class ViewHolder {
        TextView productName;
    }
}
