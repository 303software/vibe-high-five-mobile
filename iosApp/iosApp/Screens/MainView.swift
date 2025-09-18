import SwiftUI

struct MainView: View {
    @StateObject private var navigationViewModel = NavigationViewModel()
    
    var body: some View {
        ZStack {
            VStack(spacing: 0) {
                // Toolbar
                AppToolbar(title: navigationViewModel.screenTitle) {
                    navigationViewModel.toggleMenu()
                }
                
                // Main content area
                ZStack {
                    switch navigationViewModel.currentScreen {
                    case .screen1:
                        UsersView()
                    case .screen2:
                        LeaderboardView()
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
            
            // Slide-out menu overlay
            if navigationViewModel.isMenuOpen {
                // Background overlay to close menu when tapped
                Color.black.opacity(0.3)
                    .ignoresSafeArea()
                    .onTapGesture {
                        navigationViewModel.closeMenu()
                    }
                
                // Slide-out menu
                HStack {
                    SlideOutMenu(
                        onScreen1Tapped: {
                            navigationViewModel.navigateToScreen(.screen1)
                        },
                        onScreen2Tapped: {
                            navigationViewModel.navigateToScreen(.screen2)
                        },
                        onLogoutTapped: {
                            navigationViewModel.showLogoutConfirmation()
                        },
                        onClose: {
                            navigationViewModel.closeMenu()
                        }
                    )
                    .transition(.move(edge: .leading))
                    
                    Spacer()
                }
            }
        }
        .animation(.easeInOut(duration: 0.3), value: navigationViewModel.isMenuOpen)
        .alert("Log Out", isPresented: $navigationViewModel.showLogoutDialog) {
            Button("Cancel", role: .cancel) { }
            Button("Log Out", role: .destructive) {
                navigationViewModel.logout()
            }
        } message: {
            Text("Are you sure you want to log out?")
        }
    }
}
