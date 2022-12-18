package com.example.appinteligentes;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @POST("predict")
    Call<ResponseModel> createPost(@Body ImageModel imageModel);
}
