package com.phantom.sample.api;


import android.graphics.PostProcessor;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface Api {

    @POST("/user/")
    Observable<Boolean> createUser(@Body CreateUserRequest createUserRequest);

    @GET("/user/{userAccount}")
    Observable<UserResponse> getUser(@Path("userAccount") String account);

    @GET("/group/list")
    Observable<List<GroupResponse>> listGroup(@Query("page") int page, @Query("size") int size);

    @GET("/user/list")
    Observable<List<UserResponse>> listUser(@Query("userAccount") String userAccount, @Query("page") int mPage, @Query("size") int size);

    @POST("/group/joinGroup")
    Observable<Boolean> joinGroup(@Body JoinGroupRequest request);
}
