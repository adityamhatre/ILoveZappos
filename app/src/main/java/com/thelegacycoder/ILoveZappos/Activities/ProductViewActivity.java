package com.thelegacycoder.ILoveZappos.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thelegacycoder.ILoveZappos.AppController.AppController;
import com.thelegacycoder.ILoveZappos.Models.ProductItem;
import com.thelegacycoder.ILoveZappos.R;
import com.thelegacycoder.ILoveZappos.databinding.ActivityProductViewBinding;

public class ProductViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ActivityProductViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_product_view);

        int productIndex = getIntent().getIntExtra("productIndex", 0);

        ProductItem productItem = AppController.getInstance().getProducts().get(productIndex);
        binding.setProduct(productItem);

        //System.out.println("PID: " + productItem.getProductId());

        if (productItem.getPercentOff().replace(" OFF", "").trim().equalsIgnoreCase("0%")) {
            System.out.println("hiding");
            findViewById(R.id.offerDetails).setVisibility(View.GONE);
            findViewById(R.id.productSaved).setVisibility(View.GONE);
        } else System.out.println("not hiding");

        getSupportActionBar().setTitle(productItem.getProductName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add("Cart").setIcon(R.drawable.ic_cart).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        *//*menu.add("Cart").setIcon(R.drawable.ic_cart).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        }).isVisible();*//*


        return true;

    }*/
}
