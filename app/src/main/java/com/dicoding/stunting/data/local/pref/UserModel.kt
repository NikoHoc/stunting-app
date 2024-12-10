package com.dicoding.stunting.data.local.pref

data class UserModel(
    val userId: String,
    val name: String,
    val email: String,
    val password: String,
    val token: String,
    val isLogin: Boolean = false
)