package com.example.ibitest.model

import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ibitest.utils.StringListConverter

@Stable
data class ProductsResponse(
    @SerializedName("products")
    val results: List<Product>
)

@Stable
@Entity(tableName = "favorite_products_table")
@TypeConverters(StringListConverter::class)
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
    val brand: String = "",

    @SerializedName("images")
    val images: List<String> = emptyList()
)
