package com.highfive.app.networking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartupData(
    @SerialName("version")
    val version: String,
    @SerialName("min_ios_version")
    val minIOSVersion: String,
    @SerialName("min_android_version")
    val minAndroidVersion: String,
    @SerialName("api_url")
    val apiUrl: String,
)

@Serializable
data class SuccessResponse(
    @SerialName("success")
    val success: Boolean
)

@Serializable
data class CustomAPIError(
    val message: String
)

@Serializable
data class LoginPayload(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
    @SerialName("expiresInMins") val expiresInMins: Int = 30
)

@Serializable
data class AuthTokenResponse(
    @SerialName("accessToken") val accessToken: String? = null,
    @SerialName("refreshToken") val refreshToken: String? = null,
    @SerialName("errorMessage") var errorMessage: String? = null,
)

@Serializable
data class RefreshTokenPayload(
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("expiresInMins") val expiresInMins: Int = 30
)

@Serializable
data class Boost(
    @SerialName("id") val id: String? = null,
    @SerialName("receiver") val receiver: String,
    @SerialName("sender") val sender: String,
    @SerialName("type") val type: String,
)

@Serializable
data class User(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
)
