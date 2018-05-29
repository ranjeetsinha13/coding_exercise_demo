package com.rs.mylivn.entity

import com.google.gson.annotations.SerializedName

data class ImagesData(@SerializedName("items") val images: ArrayList<ImageItem>)
data class ImageItem(@SerializedName("uuid") val uuid: String,
                     @SerializedName("imageUrlString") val imageURL: String)
