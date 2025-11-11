package com.example.qfood.apis

import com.example.qfood.item.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ServiceInterface {
    @Headers("Content-Type:application/json")
    @GET("users/{email}/")
    fun getUserByEmail(@Path("email") email: String): Call<User>

    @Headers("Content-Type:application/json")
    @POST("users/")
    fun createUser(@Body data: UserPost): Call<Any>

    @Headers("Content-Type:application/json")
    @GET("foods/")
    suspend fun getFoods(): Response<MutableList<Food>>

    @Headers("Content-Type:application/json")
    @GET("cartItem/{user_id}/")
    suspend fun getCart(@Path("user_id") user_id: Int): Response<MutableList<CartItem>>

    @Headers("Content-Type:application/json")
    @GET("cartItem/{user_id}/{food_id}/")
    suspend fun getCartItem(
        @Path("user_id") user_id: Int,
        @Path("food_id") food_id: Int
    ): Response<MutableList<CartItem>>

    @Headers("Content-Type:application/json")
    @POST("cartItem/")
    fun createCartItem(@Body item: CartItem): Call<Void?>

    @Headers("Content-Type:application/json")
    @PUT("cartItem/")
    fun updateCartItem(@Body item: CartItem): Call<Void?>

    @Headers("Content-Type:application/json")
    @DELETE("cartItem/{user_id}/")
    fun deleteCart(@Path("user_id") user_id: Int): Call<Void?>

    @Headers("Content-Type:application/json")
    @DELETE("cartItem/{user_id}/{food_id}/")
    fun deleteCartItem(@Path("user_id") user_id: Int, @Path("food_id") food_id: Int): Call<Void?>

    @Headers("Content-Type:application/json")
    @GET("billstatus/new/")
    suspend fun getBillId(): Response<BillIdResponse?>

    @Headers("Content-Type:application/json")
    @POST("billdetails/")
    fun createBillDetail(@Body item: BillDetail): Call<Void?>

    @Headers("Content-Type:application/json")
    @POST("billstatus/")
    fun createBillStatus(@Body item: BillStatus): Call<Void?>

    @Headers("Content-Type:application/json")
    @GET("billstatus/user/{user_id}")
    suspend fun getAllBill(@Path("user_id") user_id: Int): Response<MutableList<BillStatus>>

    @Headers("Content-Type:application/json")
    @GET("billstatus/")
    suspend fun getAllBills(): Response<MutableList<BillStatus>>

    @Headers("Content-Type:application/json")
    @PUT("billstatus/{bill_id}")
    fun updateBillStatus(@Path("bill_id") bill_id: Int): Call<Void?>

    @Headers("Content-Type:application/json")
    @PUT("billstatus/cancel/{bill_id}")
    fun cancelBill(@Path("bill_id") bill_id: Int): Call<Void?>

    @Headers("Content-Type:application/json")
    @PUT("billstatus/paid/{bill_id}")
    fun paidBill(@Path("bill_id") bill_id: Int): Call<Void?>
}