package com.koala.mayintarlasi.services;

import com.koala.mayintarlasi.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProjectService
{
    @GET("user/all")
    Call<List<UserModel>> getAllUsers();

    @GET("user/difficulty/0")
    Call<List<UserModel>> getEasy();

    @GET("user/difficulty/1")
    Call<List<UserModel>> getMedium();

    @GET("user/difficulty/2")
    Call<List<UserModel>> getHard();

    @POST("user/add")
    Call<String> sendUserScore(@Body UserModel user);
}
