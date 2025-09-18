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

    suspend fun refreshSession(): Boolean {
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
                return false
            }
        } else {
            return false
        }
        return true
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
        return supabase.postgrest.from("boost").select(Columns.ALL).decodeList<Boost>()
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
        val boosts = supabase.postgrest.from("boost").select(Columns.ALL).decodeList<Boost>()
        return boosts
    }

    suspend fun getBoosts(): List<Boost> {
        return supabase.postgrest.from("boost").select(Columns.ALL) {
            order("created_at", Order.DESCENDING)
        }.decodeList<Boost>()
    }

    suspend fun addBoost(boost: Boost): List<Boost> {
        return supabase.postgrest.from("boost").insert(boost) {
            select(Columns.ALL)
            order("created_at", Order.DESCENDING)
        }.decodeList<Boost>()
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