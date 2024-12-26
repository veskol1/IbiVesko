package com.example.ibitest.model

import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.PrimaryKey

@Stable
data class ProductsResponse(
    @SerializedName("products")
    val results: List<Product>
)

@Stable
@Entity(tableName = "favorite_products_table")
data class Product(
    @PrimaryKey
    val id: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("thumbnail")
    val image: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("price")
    val price: String = "",

    @SerializedName("brand")
    val brand: String = ""
)
