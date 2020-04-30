package com.example.assignmentandroidserver.network

import com.example.assignmentandroidserver.model.*
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object API {
    const val BASE_URL = "https://quanlybanhangtest.herokuapp.com/"

    val service: AppRepository by lazy {
        val logging = HttpLoggingInterceptor()
        val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //(*)
            .client(client)
            .build().create<AppRepository>(AppRepository::class.java)
    }

    interface AppRepository {
        @FormUrlEncoded
        @POST("login")
        fun login(@Field("username") username: String, @Field("password") password: String): Observable<LoginResponse>
        @FormUrlEncoded
        @POST("register")
        fun signUp(@Field("username") username: String, @Field("password") password: String): Observable<RegisterResponse>

        @GET("getAllProduct")
        fun getAllProduct():Observable<GetAllProductReponse>
        @FormUrlEncoded
        @POST("getCartById")
        fun getAllCartItem(@Field("username") username: String):Observable<GetCartByIdResponse>
        @POST("addItemToCart")
        @FormUrlEncoded
        fun addItemToCart(@Field("username") username: String, @Field("productName") productName: String,@Field("productType") productType: String,@Field("price") price: Int):Observable<AddItemToCartResponse>
    }
}