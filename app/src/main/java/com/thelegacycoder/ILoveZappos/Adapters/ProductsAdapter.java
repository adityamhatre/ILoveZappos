package com.thelegacycoder.ILoveZappos.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thelegacycoder.ILoveZappos.Activities.CartActivity;
import com.thelegacycoder.ILoveZappos.AppController.AppController;
import com.thelegacycoder.ILoveZappos.Models.ProductItem;
import com.thelegacycoder.ILoveZappos.R;

import java.util.List;

/**
 * Created by Aditya on 002, 2 Feb, 2017.
 */

public class ProductsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<ProductItem> productItemList;
    private Boolean isCart = false;
    private AlertDialog removeFromCartDialog;

    public ProductsAdapter(Context context, List<ProductItem> productItemList) {
        this.context = context;
        this.productItemList = productItemList;
    }

    public ProductsAdapter(Context context, List<ProductItem> productItemList, Boolean isCart) {
        this.context = context;
        this.productItemList = productItemList;
        this.isCart = isCart;
        removeFromCartDialog = new AlertDialog.Builder(context).setTitle("Confirm").setMessage("Remove from cart?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("No", null).setNeutralButton("Cancel", null).create();
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
    public View getView(final int index, View convertView, ViewGroup viewGroup) {
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
            viewHolder.removeFromCart = (ImageView) convertView.findViewById(R.id.remove_from_cart);

            if (isCart) viewHolder.removeFromCart.setVisibility(View.VISIBLE);
            else viewHolder.removeFromCart.setVisibility(View.GONE);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ProductItem productItem = (ProductItem) getItem(index);

        viewHolder.productName.setText(productItem.getProductName());
        viewHolder.productBrand.setText(productItem.getBrandName());
        viewHolder.productPrice.setText(productItem.getPrice());

        viewHolder.removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromCartDialog.show();
                removeFromCartDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppController.getInstance().removeFromCart(index);
                        ProductsAdapter.this.notifyDataSetChanged();
                        if (removeFromCartDialog.isShowing()) removeFromCartDialog.dismiss();
                    }
                });


            }
        });
        Picasso.with(context)
                .load(productItem.getThumbnailImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(viewHolder.productImage);

        return convertView;
    }


    private static class ViewHolder {
        TextView productName, productBrand, productPrice;
        ImageView productImage, removeFromCart;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (isCart) {
            if (getCount() == 0) CartActivity.showEmptyView();
            else CartActivity.hideEmptyView();
        }
    }
}
