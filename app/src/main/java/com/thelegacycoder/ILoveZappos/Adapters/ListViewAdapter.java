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

    private Context context;
    private LayoutInflater inflater;
    private List<ProductItem> productItemList;

    public ListViewAdapter(Context context, List<ProductItem> productItemList) {
        this.context = context;
        this.productItemList = productItemList;
    }

    @Override
    public int getCount() {
        return productItemList.size();
    }

    @Override
    public Object getItem(int index) {
        return productItemList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_tile, null);
            viewHolder = new ViewHolder();
            viewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.productBrand = (TextView) convertView.findViewById(R.id.productBrand);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.productPrice);
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.product_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ProductItem productItem = (ProductItem) getItem(index);

        viewHolder.productName.setText(productItem.getProductName());
        viewHolder.productBrand.setText(productItem.getBrandName());
        viewHolder.productPrice.setText(productItem.getPrice());
        Picasso.with(context)
                .load(productItem.getThumbnailImageUrl())
                .into(viewHolder.productImage);

        return convertView;
    }


    private static class ViewHolder {
        TextView productName, productBrand, productPrice;
        ImageView productImage;
    }
}
