package com.highfive.app.networking

import com.highfive.app.logging.Logger
import io.ktor.client.plugins.logging.LogLevel
import com.highfive.app.storage.LocalStore
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.query.Order

class NetworkClient {
    private val supabase = createSupabaseClient(
        supabaseUrl = "https://klrigaoubggmuyxsphiu.supabase.co",
        supabaseKey = "sb_publishable_qq7Nzm0mcSVYJxyH3J3nwg_veVA8WNz"
    )  {
        install(Auth)
        install(Postgrest)
    }

    suspend fun logout() {
        supabase.auth.signOut()
        localStore.setString(REFRESH_TOKEN, "")
    }

    suspend fun refreshSession(): List<Boost>? {
        val token = localStore.getString(REFRESH_TOKEN, "")
        if (token.isNotEmpty()) {
            try {
                val session = supabase.auth.refreshSession(token)
                supabase.auth.importSession(session)
                val token = session.refreshToken
                localStore.setString(REFRESH_TOKEN, token)
            } catch (e: Exception) {
                Logger.e(LOGGER_TAG, "Refresh session error: ", e)
                localStore.setString(REFRESH_TOKEN, "")
                return null
            }
        } else {
            return null
        }
        return supabase.postgrest.from("boost").select(Columns.ALL){
            order("created_at", Order.DESCENDING)
        }.decodeList<Boost>()
    }

    suspend fun createAccount(email: String, password: String): List<Boost>? {
        try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            localStore.setString(REFRESH_TOKEN, supabase.auth.currentSessionOrNull()?.refreshToken ?: "")
        } catch (e: Exception) {
            Logger.e(LOGGER_TAG, "Create account error: ", e)
            return null
        }
        return supabase.postgrest.from("boost").select(Columns.ALL){
            order("created_at", Order.DESCENDING)
        }.decodeList<Boost>()
    }

    suspend fun login(email: String, password: String): List<Boost>? {
        try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val token = supabase.auth.currentSessionOrNull()?.refreshToken ?: ""
            localStore.setString(REFRESH_TOKEN, token)
        } catch (e: Exception) {
            Logger.e(LOGGER_TAG, "Sign in error: ", e)
            return null
        }
        val boosts = supabase.postgrest.from("boost").select(Columns.ALL){
            order("created_at", Order.DESCENDING)
        }.decodeList<Boost>()
        return boosts
    }

    suspend fun getBoosts(): List<Boost> {
        return supabase.postgrest.from("boost").select(Columns.ALL) {
            order("created_at", Order.DESCENDING)
        }.decodeList<Boost>()
    }

    suspend fun addBoost(receiverId: String): List<Boost> {
        val senderId = supabase.auth.currentSessionOrNull()?.user?.id ?: ""
        val boost = Boost(receiver = receiverId, sender = senderId, type = "high_five")
        return supabase.postgrest.from("boost").insert(boost) {
            select(Columns.ALL)
            order("created_at", Order.DESCENDING)
        }.decodeList<Boost>()
    }

    suspend fun getUsers(): List<User> {
        try {
            // Get authenticated users from Supabase auth.users table
            // Note: This requires RLS (Row Level Security) to be properly configured
            // and the user to have appropriate permissions to read from auth.users
            val users = supabase.postgrest.from("auth.users").select("id, email") {
                order("email", Order.ASCENDING)
            }.decodeList<User>()
            
            return users
            
        } catch (e: Exception) {
            Logger.e(LOGGER_TAG, "Get users error: ", e)
            
            // Fallback: If auth.users is not accessible, try to get users from a profiles table
            // This is a common pattern where user profile data is stored separately
            try {
                val profiles = supabase.postgrest.from("profiles").select("id, email") {
                    order("email", Order.ASCENDING)
                }.decodeList<User>()
                
                return profiles
                
            } catch (profilesError: Exception) {
                Logger.e(LOGGER_TAG, "Get profiles error: ", profilesError)
                
                // Last fallback: Try to get current session user information
                val currentUser = supabase.auth.currentUserOrNull()
                return if (currentUser != null && currentUser.email != null) {
                    listOf(User(id = currentUser.id, email = currentUser.email!!))
                } else {
                    emptyList()
                }
            }
        }
    }

    class CustomHttpLogger : io.ktor.client.plugins.logging.Logger {
        override fun log(message: String) {
            Logger.d(LOGGER_TAG, message+"\n")
        }
    }

    companion object {
        private val localStore = LocalStore()

        private val LOG_LEVEL = LogLevel.NONE
        //        private val LOG_LEVEL = LogLevel.ALL
        const val LOGGER_TAG = "HIGHFIVEAPP"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }
}