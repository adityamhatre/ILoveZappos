package com.thelegacycoder.ILoveZappos.Activities;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
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

    private Toolbar toolbar;
    private Boolean isGoingToCart = false;
    private Boolean isShaking = false;
    private Call<ProductAPIResponse> call;
    private ActivityProductViewBinding binding;
    private ProductItem productItem;
    private DetailedProductItem detailedProductItem;
    private ImageView productImage;
    private int productIndex;
    private String searchTerm;
    private Boolean sharing;
    private View productImageCopy;
    private int shakeCounter = 0;
    private View floatingActionButton;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_view);


        initViews();
        initListeners();


        try {
            productIndex = getIntent().getIntExtra("productIndex", 0);
            searchTerm = getIntent().getStringExtra("searchTerm");
            sharing = getIntent().getBooleanExtra("sharing", false);
            productItem = AppController.getInstance().getProducts().get(productIndex);
            detailedProductItem = new DetailedProductItem();

            binding.setProduct(productItem);
        } catch (Exception e) {
            e.printStackTrace();
        }


        getHDImage(productItem.getProductId());


        if (productItem.getPercentOff().replace(" OFF", "").trim().equalsIgnoreCase("0%")) {
            findViewById(R.id.offerDetails).setVisibility(View.GONE);
            findViewById(R.id.productSaved).setVisibility(View.GONE);
        } else {
            findViewById(R.id.offerDetails).setVisibility(View.VISIBLE);
            findViewById(R.id.productSaved).setVisibility(View.VISIBLE);
        }


    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        productImage = (ImageView) findViewById(R.id.productImage);
        productImageCopy = findViewById(R.id.copy_productImage);
        floatingActionButton = findViewById(R.id.add_to_cart);

        invalidateOptionsMenu();
    }

    private void initListeners() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isGoingToCart) {
                    boolean add = AppController.getInstance().getProductsInCart().size() == 0;
                    for (int i = 0; i < AppController.getInstance().getProductsInCart().size(); i++) {
                        if (AppController.getInstance().getProductsInCart().get(i).getProductId().equalsIgnoreCase(productItem.getProductId())) {
                            add = false;
                            break;
                        } else {
                            add = true;
                        }
                    }

                    if (add) {
                        scaleAndTranslate();
                        AppController.getInstance().addProductToCart(productItem);
                    } else {
                        shake();
                        Toast.makeText(ProductViewActivity.this, "Already in cart", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        AppController.getInstance().setOnLoadingCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                call.cancel();
            }
        });

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShaking) {
                    shake();
                    if (shakeCounter == 3) {
                        Toast.makeText(ProductViewActivity.this, "Click the cart icon to add product to cart", Toast.LENGTH_SHORT).show();
                        shakeCounter = 0;
                    }
                }
            }
        });


    }


    private void getHDImage(String productID) {
        call = AppController.getInstance().getRetrofit().create(ZapposAPI.class).productInfo("Product/" + productID, getString(R.string.zappos_key));
        call.enqueue(this);
    }

    private void shake() {
        productImageCopy.setVisibility(View.INVISIBLE);
        isShaking = true;
        shakeCounter++;

        final RotateAnimation rotateAnimation1, rotateAnimation2, rotateAnimation3;

        int duration = 50;
        float degrees = 20;
        rotateAnimation1 = new RotateAnimation(0, -degrees, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation1.setDuration(duration);
        rotateAnimation1.setFillAfter(true);

        rotateAnimation2 = new RotateAnimation(-degrees, degrees, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation2.setDuration(2 * duration);
        rotateAnimation2.setFillAfter(true);

        rotateAnimation3 = new RotateAnimation(degrees, 0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation3.setDuration(duration);
        rotateAnimation3.setFillAfter(true);


        rotateAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                productImage.startAnimation(rotateAnimation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rotateAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                productImage.startAnimation(rotateAnimation3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rotateAnimation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isShaking = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        productImage.startAnimation(rotateAnimation1);

    }

    private void scaleAndTranslate() {


        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(false);
        animationSet.setDuration(600);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());


        TranslateAnimation translate = new TranslateAnimation(
                0,
                floatingActionButton.getX() - productImageCopy.getX() + floatingActionButton.getPaddingLeft(),
                0,
                floatingActionButton.getY() - productImageCopy.getY() + floatingActionButton.getPaddingBottom());
        ScaleAnimation scale = new ScaleAnimation(1f, .2f, 1f, .2f);
        AlphaAnimation alpha = new AlphaAnimation(1, 0);

        animationSet.addAnimation(alpha);
        animationSet.addAnimation(scale);
        animationSet.addAnimation(translate);


        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isGoingToCart = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                productImageCopy.setVisibility(View.INVISIBLE);
                isGoingToCart = false;
                Toast.makeText(ProductViewActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();

                menu.getItem(0).setIcon(R.drawable.ic_cart_full);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        productImageCopy.startAnimation(animationSet);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RotateAnimation rotateAnimation = new RotateAnimation(0, -360f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(500);
                floatingActionButton.startAnimation(rotateAnimation);
            }
        }, 300);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        menu.add("Cart").setIcon(AppController.getInstance().getProductsInCart() != null && AppController.getInstance().getProductsInCart().size() != 0 ? R.drawable.ic_cart_full : R.drawable.ic_cart).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (AppController.getInstance().getProductsInCart().size() != 0) {
                    startActivity(new Intent(ProductViewActivity.this, CartActivity.class));
                } else {
                    Toast.makeText(ProductViewActivity.this, "No items in cart", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add("Share").setIcon(R.drawable.ic_share).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String share = "Check out this product on Zappos: " + productItem.getProductName();
                share += ("\n\nhttp://share.zappos.com/zappos/search=" + searchTerm + "&index=" + productIndex + "");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, share);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResponse(Call<ProductAPIResponse> call, Response<ProductAPIResponse> response) {
        try {
            detailedProductItem.setDefaultImageUrl(response.body().getProduct().get(0).getDefaultImageUrl());
            binding.setDetailedProduct(detailedProductItem);

        } catch (Exception e) {
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show();
            onBackPressed();
            e.printStackTrace();
        }


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (sharing)
            setResult(200);
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        if (menu != null && menu.getItem(0) != null) {
            if (AppController.getInstance().getProductsInCart().size() != 0)
                menu.getItem(0).setIcon(R.drawable.ic_cart_full);
            else menu.getItem(0).setIcon(R.drawable.ic_cart);
        }
        super.onResume();
    }

    @Override
    public void onFailure(Call<ProductAPIResponse> call, Throwable t) {

    }

}
