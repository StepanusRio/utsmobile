package com.example.utsaplikasimobiledatabuku.API;

import com.example.utsaplikasimobiledatabuku.Model.Value;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetBookAPI {
    @GET("index.php")
    Call<Value> get();
}
