import Combine
import SwiftUI

class RootViewModel: ObservableObject {
    enum startupState {
        case working
        case login
        case createAccount
        case complete
    }
    @Published var startupState = startupState.working
    @Published var boosts = [Boost]()
    @Published var users = [User]()
    
    init() {
        checkSession()
    }

    func setStartupState(newState: startupState) {
        DispatchQueue.main.async {
            self.startupState = newState
        }
    }
    
    func loadUsers() {
        iOSApp.networkClient.getUsers() { users, error in
            if let users = users {
                DispatchQueue.main.async {
                    self.users = users
                }
            } else {
                print("Error loading users: \(error?.localizedDescription ?? "Unknown error")")
            }
        }
    }
    
    private func checkSession() {
        let apiRefreshToken = iOSApp.apiRefreshToken
        if apiRefreshToken.isEmpty {
            self.setStartupState(newState: .login)
        } else {
            refreshSession()
        }
    }
    
    func refreshSession() {
        iOSApp.networkClient.refreshSession() { boosts, error in
            if (boosts != nil) {
                DispatchQueue.main.async {
                    self.boosts = boosts!
                }
                self.setStartupState(newState: .complete)
                // Also load users when session is refreshed successfully
                self.loadUsers()
            } else {
                self.setStartupState(newState: .login)
            }
        }
    }
}
