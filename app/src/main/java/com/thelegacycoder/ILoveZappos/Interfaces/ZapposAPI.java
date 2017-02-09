package com.thelegacycoder.ILoveZappos.Interfaces;

import com.thelegacycoder.ILoveZappos.Models.ProductAPIResponse;
import com.thelegacycoder.ILoveZappos.Models.SearchAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Aditya on 001, 1 Feb, 2017.
 */

public interface ZapposAPI {
    @GET("/Search")
    Call<SearchAPIResponse> searchQuery(@Query("term") String tag, @Query("key") String key);

    @GET()
    Call<ProductAPIResponse> productInfo(@Url String url, @Query("key") String key);
}