package com.thelegacycoder.ILoveZappos.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thelegacycoder.ILoveZappos.Models.ProductItem;
import com.thelegacycoder.ILoveZappos.R;

import java.util.List;

/**
 * Created by Aditya on 002, 2 Feb, 2017.
 */

public class ListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<ProductItem> productItemList;

    public ListViewAdapter(Context context, List<ProductItem> productItemList) {
        this.context = context;
        this.productItemList = productItemList;
    }

    @Override
    public void notifyDataSetChanged() {
        System.out.println(getItem(0));
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return productItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return productItemList.get(i);
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
            convertView = inflater.inflate(R.layout.product_tile, null);
            viewHolder = new ViewHolder();
            viewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.product_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ProductItem productItem = (ProductItem) getItem(i);
        viewHolder.productName.setText(productItem.getProductName());
        Picasso.with(context)
                .load(((ProductItem) (getItem(i))).getThumbnailImageUrl())
                .into(viewHolder.productImage);

        return convertView;
    }


    private static class ViewHolder {
        TextView productName;
        ImageView productImage;
    }
}
