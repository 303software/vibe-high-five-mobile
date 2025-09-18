package com.highfive.app.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.highfive.app.networking.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class StartupActivity : AppCompatActivity() {
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSession()
    }

    private fun checkSession() {
        val refreshToken = HighFiveApplication.localStore.getString(NetworkClient.REFRESH_TOKEN, "")
        if (refreshToken.isEmpty()) {
            startActivity(Intent(this@StartupActivity, LoginActivity::class.java))
            finish()
        } else {
            refreshSession()
        }
    }

    private fun refreshSession() {
        mainScope.launch {
            val ok = withContext(Dispatchers.IO) {
                withTimeoutOrNull(15_000) {
                    try {
                        val response = HighFiveApplication.networkClient.refreshSession()
                        if (response != null) {
                            HighFiveApplication.boosts = response
                            true
                        } else {
                            false
                        }
                    } catch (_: Exception) {
                        false
                    }
                } == true
            }
            var intent = Intent(this@StartupActivity, LoginActivity::class.java)
            if (ok) {
                intent = Intent(this@StartupActivity, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}
