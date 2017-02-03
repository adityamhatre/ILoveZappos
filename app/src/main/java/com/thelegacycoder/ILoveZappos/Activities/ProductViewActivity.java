package com.thelegacycoder.ILoveZappos.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.thelegacycoder.ILoveZappos.AppController.AppController;
import com.thelegacycoder.ILoveZappos.Models.ResultsItem;
import com.thelegacycoder.ILoveZappos.R;
import com.thelegacycoder.ILoveZappos.databinding.ActivityProductViewBinding;

public class ProductViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActivityProductViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_product_view);

        int productIndex = getIntent().getIntExtra("productIndex", 0);

        ResultsItem resultsItem = AppController.getInstance().getProducts().get(productIndex);
        binding.setProduct(resultsItem);

        if (resultsItem.getPercentOff().replace(" OFF", "").trim().equalsIgnoreCase("0%")) {
            System.out.println("hiding");
            //  findViewById(R.id.offerDetails).setVisibility(View.GONE);
        } else System.out.println("not hiding");

        System.out.println(resultsItem);
    }
}
