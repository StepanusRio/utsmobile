package com.example.utsaplikasimobiledatabuku.API;

import com.example.utsaplikasimobiledatabuku.Model.Value;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DeleteBookAPI {
    @FormUrlEncoded
    @POST("delete-book.php")
    Call<Value> delete(@Field("isbn")String isbn);
}
