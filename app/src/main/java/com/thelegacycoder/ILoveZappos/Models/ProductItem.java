package com.thelegacycoder.ILoveZappos.Models;

import android.databinding.BindingAdapter;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thelegacycoder.ILoveZappos.R;

import java.text.DecimalFormat;

public class ProductItem {
    private String brandName;
    private String thumbnailImageUrl;
    private String productId;
    private String originalPrice;
    private String styleId;
    private String colorId;
    private String price;
    private String percentOff;
    private String productUrl;
    private String productName;

    public ProductItem(String brandName, String thumbnailImageUrl, String productId, String originalPrice, String styleId, String colorId, String price, String percentOff, String productUrl, String productName) {
        this.brandName = brandName;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.productId = productId;
        this.originalPrice = originalPrice;
        this.styleId = styleId;
        this.colorId = colorId;
        this.price = price;
        this.percentOff = percentOff;
        this.productUrl = productUrl;
        this.productName = productName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPercentOff(String percentOff) {
        this.percentOff = percentOff;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getPercentOff() {
        return percentOff + " OFF";
    }

    public String getPrice() {
        return price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public String getMoneySaved() {
        double before, after;
        before = Double.parseDouble(getOriginalPrice().trim().replace("$", "").replace(",", ""));
        after = Double.parseDouble(getPrice().trim().replace("$", ""));

        DecimalFormat df = new DecimalFormat("###.##");
        return "You save $" + df.format(before - after);
    }

    @Override
    public String toString() {
        return "ProductItem{" +
                "brandName='" + brandName + '\'' +
                ", thumbnailImageUrl='" + thumbnailImageUrl + '\'' +
                ", productId='" + productId + '\'' +
                ", originalPrice='" + originalPrice + '\'' +
                ", styleId='" + styleId + '\'' +
                ", colorId='" + colorId + '\'' +
                ", price='" + price + '\'' +
                ", percentOff='" + percentOff + '\'' +
                ", productUrl='" + productUrl + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }


    @BindingAdapter({"bind:imageUrlAnimate"})
    public static void loadImageAnimate(final ImageView view, String thumbnailImageUrl) {

        Picasso.with(view.getContext())
                .load(thumbnailImageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
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
                                        view.startAnimation(rotateAnimation2);
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
                                        view.startAnimation(rotateAnimation3);
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
                                        view.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                view.startAnimation(rotateAnimation1);
                            }
                        }, 100);

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(final ImageView view, String thumbnailImageUrl) {

        Picasso.with(view.getContext())
                .load(thumbnailImageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(view);
    }
}