import Combine
import SwiftUI

class NavigationViewModel: ObservableObject {
    enum Screen {
        case screen1
        case screen2
    }
    
    @Published var currentScreen: Screen = .screen1
    @Published var isMenuOpen: Bool = false
    @Published var showLogoutDialog: Bool = false
    
    init() {
    }
    
    func navigateToScreen(_ screen: Screen) {
        DispatchQueue.main.async {
            self.currentScreen = screen
            self.isMenuOpen = false // Close menu when navigating
        }
    }
    
    func toggleMenu() {
        DispatchQueue.main.async {
            self.isMenuOpen.toggle()
        }
    }
    
    func closeMenu() {
        DispatchQueue.main.async {
            self.isMenuOpen = false
        }
    }
    
    func showLogoutConfirmation() {
        DispatchQueue.main.async {
            self.showLogoutDialog = true
            self.isMenuOpen = false
        }
    }
    
    func logout() {
        Task { @MainActor in
            do {
                let response = try await iOSApp.networkClient.logout()
                DispatchQueue.main.async {
                    iOSApp.rootViewModel.setStartupState(newState: .login)
                }
            } catch {
                print("Unable to log out")
            }
        }
    }
    
    var screenTitle: String {
        switch currentScreen {
        case .screen1:
            return "Screen 1"
        case .screen2:
            return "Screen 2"
        }
    }
}
