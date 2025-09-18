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
    
    init() {
        checkSession()
    }

    func setStartupState(newState: startupState) {
        DispatchQueue.main.async {
            self.startupState = newState
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
            } else {
                self.setStartupState(newState: .login)
            }
        }
    }
}
