package com.thelegacycoder.ILoveZappos.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;

import com.thelegacycoder.ILoveZappos.Adapters.ProductsAdapter;
import com.thelegacycoder.ILoveZappos.AppController.AppController;
import com.thelegacycoder.ILoveZappos.R;

public class CartActivity extends AppCompatActivity {

    private GridView cartGrid;
    private ProductsAdapter productsAdapter;
    private static View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Cart");
        initView();

        if (AppController.getInstance().getProductsInCart().size() == 0) showEmptyView();
        else hideEmptyView();


        productsAdapter = new ProductsAdapter(this, AppController.getInstance().getProductsInCart(), true);
        cartGrid.setAdapter(productsAdapter);

    }

    private void initView() {
        cartGrid = (GridView) findViewById(R.id.cart_list);
        emptyView = findViewById(R.id.empty_view);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
    }

    public static void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
