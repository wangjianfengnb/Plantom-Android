package com.phantom.sample.api;


import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface Api {

    @POST("/user/")
    Observable<Boolean> createUser(@Body CreateUserRequest createUserRequest);

    @GET("/user/{userAccount}")
    Observable<UserResponse> getUser(@Path("userAccount") String account);

}
