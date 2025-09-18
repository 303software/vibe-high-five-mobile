import SwiftUI
import shared

struct LoginView: View {
    @State private var email: String = ""
    @State private var password: String = ""
    @State private var isLoading: Bool = false
    @State private var errorMessage: String = ""
    @State private var showError: Bool = false
    
    var body: some View {
        ZStack {
            VStack(spacing: 24) {
                // Title
                Text("KMM Template")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(.primary)
                    .padding(.top, 64)
                
                Spacer()
                
                // Input fields
                VStack(spacing: 16) {
                    TextField("Email", text: $email)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        .autocapitalization(.none)
                        .autocorrectionDisabled()
                    
                    SecureField("Password", text: $password)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                }
                .padding(.horizontal, 24)
                
                // Login button
                Button(action: performLogin) {
                    Text("LOG IN")
                        .font(.headline)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 50)
                        .background(Color.blue)
                        .cornerRadius(8)
                }
                .padding(.horizontal, 24)
                .disabled(isLoading)
                
                // Create Account link button
                Button(action: navigateToCreateAccount) {
                    Text("Create Account")
                        .font(.subheadline)
                        .foregroundColor(.blue)
                        .underline()
                }
                .padding(.top, 8)
                
                Spacer()
            }
            
            // Loading overlay
            if isLoading {
                Color.black.opacity(0.5)
                    .ignoresSafeArea()
                
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: .white))
                    .scaleEffect(1.5)
            }
        }
        .alert("Error", isPresented: $showError) {
            Button("OK") { }
        } message: {
            Text(errorMessage)
        }
    }
    
    private func performLogin() {
        // Input validation
        guard !email.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            showErrorMessage("Please enter your email.")
            return
        }
        
        guard !password.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            showErrorMessage("Please enter your password.")
            return
        }
        
        // Show loading
        isLoading = true
        
        Task { @MainActor in
            do {
                let response = try await iOSApp.networkClient.login(
                    email: email.trimmingCharacters(in: .whitespacesAndNewlines),
                    password: password.trimmingCharacters(in: .whitespacesAndNewlines)
                )

                isLoading = false
                if response == nil {
                    showErrorMessage("Invalid email or password.")
                } else {
                    iOSApp.rootViewModel.boosts = response ?? []
                    iOSApp.rootViewModel.setStartupState(newState: .complete)
                    iOSApp.rootViewModel.loadUsers()
                }
            } catch {
                isLoading = false
                showErrorMessage("Invalid email or password.")
            }
        }
    }
    
    private func showErrorMessage(_ message: String) {
        errorMessage = message
        showError = true
    }
    
    private func navigateToCreateAccount() {
        iOSApp.rootViewModel.setStartupState(newState: .createAccount)
    }
}
