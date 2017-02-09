package com.thelegacycoder.ILoveZappos.Activities;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.thelegacycoder.ILoveZappos.AppController.AppController;
import com.thelegacycoder.ILoveZappos.Interfaces.ZapposAPI;
import com.thelegacycoder.ILoveZappos.Models.DetailedProductItem;
import com.thelegacycoder.ILoveZappos.Models.ProductAPIResponse;
import com.thelegacycoder.ILoveZappos.Models.ProductItem;
import com.thelegacycoder.ILoveZappos.R;
import com.thelegacycoder.ILoveZappos.databinding.ActivityProductViewBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewActivity extends AppCompatActivity implements Callback<ProductAPIResponse> {

    Toolbar toolbar;
    Boolean isAnimating = false;
    private Call<ProductAPIResponse> call;
    ActivityProductViewBinding binding;
    ProductItem productItem;
    DetailedProductItem detailedProductItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_view);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int productIndex = getIntent().getIntExtra("productIndex", 0);

        productItem = AppController.getInstance().getProducts().get(productIndex);

        detailedProductItem = new DetailedProductItem();
        detailedProductItem.setDefaultImageUrl("http://www.zappos.com/images/z/3/5/2/8/1/9/3528199-p-DETAILED.jpg");


        getHDImage(productItem.getProductId());


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
                if (!isAnimating)
                    scaleAndTranslate();
            }
        });


        AppController.getInstance().setOnLoadingCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                call.cancel();
            }
        });
    }

    private void getHDImage(String productID) {
        call = AppController.getInstance().getRetrofit().create(ZapposAPI.class).productInfo("Product/" + productID, getString(R.string.zappos_key));
        AppController.getInstance().showLoading(this);
        call.enqueue(this);
    }

    private void scaleAndTranslate() {
        final View view = findViewById(R.id.copy);
        final View fab = findViewById(R.id.add_to_cart);

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


        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                isAnimating = false;
                Toast.makeText(ProductViewActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animationSet);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RotateAnimation rotateAnimation = new RotateAnimation(0, -360f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(500);
                fab.startAnimation(rotateAnimation);
            }
        }, 300);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add("Cart").setIcon(R.drawable.ic_cart).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }


    @Override
    public void onResponse(Call<ProductAPIResponse> call, Response<ProductAPIResponse> response) {

        AppController.getInstance().dismissLoading();
        try {
            detailedProductItem.setDefaultImageUrl(response.body().getProduct().get(0).getDefaultImageUrl());

            binding.setProduct(productItem);
            binding.setDetailedProduct(detailedProductItem);

        } catch (Exception e) {
            onBackPressed();
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(Call<ProductAPIResponse> call, Throwable t) {

    }
}
