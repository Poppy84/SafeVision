package com.example.safevision.api;

import com.safevision.models.LoginRequest;
import com.safevision.models.LoginResponse;
import com.safevision.models.User;
import com.safevision.models.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ApiService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("register")
    Call<ApiResponse> register(@Body User user);

    @GET("system-status")
    Call<ApiResponse> getSystemStatus(@Header("Authorization") String token);
}