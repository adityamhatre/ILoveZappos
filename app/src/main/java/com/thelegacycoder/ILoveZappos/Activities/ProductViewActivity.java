package com.thelegacycoder.ILoveZappos.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.thelegacycoder.ILoveZappos.AppController.AppController;
import com.thelegacycoder.ILoveZappos.Models.ProductItem;
import com.thelegacycoder.ILoveZappos.R;
import com.thelegacycoder.ILoveZappos.databinding.ActivityProductViewBinding;

public class ProductViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    Animation hyperspaceJumpAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProductViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_product_view);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int productIndex = getIntent().getIntExtra("productIndex", 0);

        ProductItem productItem = AppController.getInstance().getProducts().get(productIndex);
        binding.setProduct(productItem);
        System.out.println(productItem.getProductId());

        if (productItem.getPercentOff().replace(" OFF", "").trim().equalsIgnoreCase("0%")) {
            findViewById(R.id.offerDetails).setVisibility(View.GONE);
            findViewById(R.id.productSaved).setVisibility(View.GONE);
        } else {
            findViewById(R.id.offerDetails).setVisibility(View.VISIBLE);
            findViewById(R.id.productSaved).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleAndTranslate();


            }
        });
    }

    private void scaleAndTranslate() {
        final View view = findViewById(R.id.copy);
        view.setVisibility(View.VISIBLE);


        View fab = findViewById(R.id.add_to_cart);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(false);
        animationSet.setDuration(600);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());


        TranslateAnimation translate = new TranslateAnimation(
                0,
                fab.getX() - view.getX() + fab.getPaddingLeft(),
                0,
                fab.getY() - view.getY() + fab.getPaddingBottom());
        ScaleAnimation scale = new ScaleAnimation(1f, .2f, 1f, .2f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);


        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scale);
        animationSet.addAnimation(translate);


        view.startAnimation(animationSet);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);
            }
        }, 370);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("i am here");
        menu.add("Cart").setIcon(R.drawable.ic_cart).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }


}
