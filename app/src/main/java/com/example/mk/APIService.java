package com.example.mk;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService{
    @GET("/")
    Call<ValueData> getContact();

    @POST("/")
    Call<ValueData> postContact(@Body Contact contact);

    @PUT("/{id}")
    Call<Contact> putContact(@Path("id") String id, @Body Contact contact);

    @DELETE("/{id}")
    Call<Void> deleteContact(@Path("id") String id);

    @FormUrlEncoded
    @POST("/auth/login")
    Call<ValueNoData> login(@Field("username")String username,
                            @Field("password")String password);
    @FormUrlEncoded
    @POST("/auth/register")
    Call<ValueNoData> register(@Field("username")String username,
                               @Field("password")String password);
}
