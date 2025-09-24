package com.example.safevision.api;

import com.example.safevision.models.LoginRequest;
import com.example.safevision.models.LoginResponse;
import com.example.safevision.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;


public interface ApiService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("register")
    Call<ResponseBody> register(@Body User user);  // ✅ Cambiado a ResponseBody

    @GET("system-status")
    Call<ResponseBody> getSystemStatus(@Header("Authorization") String token);

    @GET("alerts")
    Call<ResponseBody> getAlerts(@Header("Authorization") String token);

    @POST("upload-face")
    Call<ResponseBody> uploadFace(@Header("Authorization") String token, @Body Object faceData);
}