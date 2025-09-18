package com.highfive.app.android

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.highfive.app.android.databinding.ActivityCreateAccountBinding
import com.highfive.app.networking.NetworkClient
import kotlinx.coroutines.launch

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()

        binding.passwordEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                binding.verifyPasswordEditText.requestFocus()
                true
            } else {
                false
            }
        }
        
        binding.verifyPasswordEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                createAccount()
                true
            } else {
                false
            }
        }
    }

    private fun setupClickListeners() {
        binding.createAccountButton.setOnClickListener {
            createAccount()
        }
        binding.loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createAccount() {
        hideKeyboard()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val verifyPassword = binding.verifyPasswordEditText.text.toString().trim()

        // Input validation
        when {
            email.isEmpty() -> {
                showError("Please enter your email.")
                return
            }
            password.isEmpty() -> {
                showError("Please enter your password.")
                return
            }
            verifyPassword.isEmpty() -> {
                showError("Please verify your password.")
                return
            }
            password != verifyPassword -> {
                showError("Passwords do not match.")
                return
            }
        }

        // Show loading overlay
        showLoading(true)

        // Perform network call
        lifecycleScope.launch {
            try {
                val response = HighFiveApplication.networkClient.createAccount(email, password)

                // Hide loading overlay
                showLoading(false)

                // Check if create account was successful
                if (response == null) {
                    showError("There was a problem creating your account. Please try again later.")
                } else {
                    // Create account successful - navigate to MainActivity
                    navigateToMainActivity()
                }

            } catch (e: Exception) {
                // Hide loading overlay
                showLoading(false)

                // Show error message
                showError("There was a problem creating your account. Please try again later.")
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.loadingOverlay.visibility = if (show) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }

        // Disable/enable create account button
        binding.createAccountButton.isEnabled = !show
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish activity so user can't go back to it
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: binding.root
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
