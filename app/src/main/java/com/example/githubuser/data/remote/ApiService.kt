package com.example.githubuser.data.remote

import com.example.githubuser.data.model.DetailUserResponse
import com.example.githubuser.data.model.GithubResponse
import com.example.githubuser.data.model.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun getUser(@Query("q") query: String): GithubResponse
    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String): DetailUserResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): List<ItemsItem>
    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): List<ItemsItem>
}