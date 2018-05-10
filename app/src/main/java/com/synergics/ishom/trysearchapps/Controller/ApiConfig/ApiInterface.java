package com.synergics.ishom.trysearchapps.Controller.ApiConfig;

import com.synergics.ishom.trysearchapps.Model.RetrofitResponse.ResponseCategory;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by asmarasusanto on 10/8/17.
 */

public interface ApiInterface {

    @GET("categories.json")
    Call<ResponseCategory> getCategory();

//    @GET("english_for_fun/api/learn.php")
//    Call<Learn> getLearn(@Query("id") String id);
//
//    @GET("english_for_fun/api/mini-games.php")
//    Call<MiniGames> getMiniGames(@Query("id") String id);
//
//    @GET("getVideoByFeature.php")
//    Call<List<ResponseVideo>> getVideo(@Query("idc") String id);

}
