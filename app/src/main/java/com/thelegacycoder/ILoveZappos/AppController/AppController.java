package com.thelegacycoder.ILoveZappos.AppController;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import com.thelegacycoder.ILoveZappos.Models.ResultsItem;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aditya on 002, 2 Feb, 2017.
 */

public class AppController extends Application {
    public static final String BASE_URL = "https://api.zappos.com/";
    private static AppController appController;
    private Retrofit retrofit;

    private Context context;
    private ProgressDialog progressDialog;
    private List<ResultsItem> products;

    @Override
    public void onCreate() {
        super.onCreate();
        initRetroFit();
        appController = this;
    }

    private void initRetroFit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized AppController getInstance() {
        return appController;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }


    public void showLoading(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(this.context);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void dismissLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void setProducts(List<ResultsItem> products) {
        this.products = products;
    }

    public List<ResultsItem> getProducts() {
        return products;
    }
}
