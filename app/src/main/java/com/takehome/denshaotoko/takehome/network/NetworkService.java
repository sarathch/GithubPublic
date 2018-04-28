package com.takehome.denshaotoko.takehome.network;

import com.takehome.denshaotoko.takehome.data.Repo;
import com.takehome.denshaotoko.takehome.data.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NetworkService {

    @GET("/users/{userId}")
    Observable<User> getUser(
            @Path("userId") String userId
    );

    @GET("/users/{userId}/repos")
    Observable<List<Repo>> getUsersRepositories(@Path("userId") String userId);

}
