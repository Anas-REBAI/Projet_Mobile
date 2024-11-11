package tn.esprit.nascar.utils

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import tn.esprit.nascar.models.Historique
import tn.esprit.nascar.models.Product
import tn.esprit.nascar.models.findedUser
import tn.esprit.nascar.models.User

interface ApiInterface {

    @POST("api/user/login")
    fun login(@Body user: User): Call<findedUser>

    @GET("api/product/getAllProducts")
    fun getAllProducts(): Call<List<Product>>

    @GET("api/product/getProductById/{productId}")
    fun getProductById(@Path("productId") productId: String): Call<Product>

    @GET("api/history/getUserHistory/{userId}")
    fun getUserHistory(@Path("userId") userId: String): Call<List<Historique>>

    @DELETE("api/history/deleteFromHistory/{historyId}")
    fun deleteFromHistory(@Path("historyId") historyId: String): Call<Void>

    companion object {
        var BASE_URL = "http://192.168.1.151:3000/"

        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}