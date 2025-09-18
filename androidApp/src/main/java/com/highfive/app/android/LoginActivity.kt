package com.highfive.app.android

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.highfive.app.android.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()

        binding.passwordEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                performLogin()
                true
            } else {
                false
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            performLogin()
        }
        
        binding.createAccountLink.setOnClickListener {
            navigateToCreateAccount()
        }
    }
    
    private fun performLogin() {
        hideKeyboard()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        
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
        }
        
        // Show loading overlay
        showLoading(true)
        
        // Perform network call
        lifecycleScope.launch {
            try {
                val response = HighFiveApplication.networkClient.login(email, password)
                
                // Hide loading overlay
                showLoading(false)
                
                // Check if login was successful
                if (response == null) {
                    showError("Invalid email or password.")
                } else {
                    // Login successful - navigate to MainActivity
                    navigateToMainActivity()
                }
                
            } catch (e: Exception) {
                // Hide loading overlay
                showLoading(false)
                
                // Show error message
                showError("Invalid email or password.")
            }
        }
    }
    
    private fun showLoading(show: Boolean) {
        binding.loadingOverlay.visibility = if (show) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
        
        // Disable/enable login button
        binding.loginButton.isEnabled = !show
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
        finish() // Finish login activity so user can't go back to it
    }
    
    private fun navigateToCreateAccount() {
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: binding.root
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
