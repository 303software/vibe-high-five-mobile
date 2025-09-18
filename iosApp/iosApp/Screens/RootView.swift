import SwiftUI
import shared

struct RootView: View {
    @StateObject var viewModel: RootViewModel=iOSApp.rootViewModel
    
    var body: some View {
        ZStack {
            switch viewModel.startupState {
                case .working:
                    EmptyView()
                case .login:
                    LoginView()
                case .createAccount:
                    CreateAccountView()
                case .complete:
                    MainView()
            }
        }
    }
}
