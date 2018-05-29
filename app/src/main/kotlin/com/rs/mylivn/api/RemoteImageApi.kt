package com.rs.mylivn.api

import com.rs.mylivn.entity.ImageItem
import io.reactivex.Single
import retrofit2.http.*

interface RemoteImageApi {

    @GET("/images")
    fun getImages(): Single<List<ImageItem>>

    @FormUrlEncoded
    @POST("/addImage")
    fun addImage(@Field("imageUrl") imageUrl: String): Single<ImageItem>

    @DELETE("/deleteImage/{uuid}")
    fun deleteImage(@Path("uuid") uuid: String)

    @GET("/image/{id}")
    fun getImage(@Path("uuid") uuid: String): Single<ImageItem>

    @POST("images/swap{fromPosition}{toPosition}")
    fun swapImages(@Path("fromPosition") fromPosition: Int, @Path("toPosition") toPosition: Int)


}