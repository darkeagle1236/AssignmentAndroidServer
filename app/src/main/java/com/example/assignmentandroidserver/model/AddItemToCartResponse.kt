package com.example.assignmentandroidserver.model

data class AddItemToCartResponse(
    val __v: Int,
    val _id: String,
    val price: Int,
    val productName: String,
    val productType: String,
    val quantity: Int,
    val username: String
)