package com.koala.mayintarlasi.services;
import com.koala.mayintarlasi.helpers.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null ;
    public  static  Retrofit getClient()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit= new Retrofit.Builder().baseUrl(Constants.ServiceUrl).addConverterFactory(
                GsonConverterFactory.create()
        ).client(client).build();
        return retrofit;
    }

    public  static  Retrofit getClient(String yol)
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit= new Retrofit.Builder().baseUrl(yol).addConverterFactory(
                GsonConverterFactory.create()
        ).client(client).build();
        return retrofit;
    }
}