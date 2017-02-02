package com.thelegacycoder.ILoveZappos.Interfaces;

import com.thelegacycoder.ILoveZappos.Models.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Aditya on 001, 1 Feb, 2017.
 */

public interface ZapposAPI {
    @GET("/Search")
    Call<SearchResponse> searchQuery(@Query("term") String tag, @Query("key") String key);
}