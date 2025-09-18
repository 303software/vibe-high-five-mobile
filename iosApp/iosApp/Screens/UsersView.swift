import SwiftUI

struct UsersView: View {
    @StateObject var viewModel: RootViewModel = iOSApp.rootViewModel
    
    var body: some View {
        VStack(alignment: .leading) {
            // Title
            Text("Users")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.primary)
                .padding(.horizontal)
                .padding(.top)
            
            // Users List
            if viewModel.users.isEmpty {
                VStack {
                    Spacer()
                    Text("No users found")
                        .font(.body)
                        .foregroundColor(.secondary)
                    Text("Pull to refresh or check your connection")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Spacer()
                }
            } else {
                List(viewModel.users, id: \.id) { user in
                    VStack(alignment: .leading, spacing: 4) {
                        Text(user.email)
                            .font(.body)
                            .foregroundColor(.primary)
                        Text("ID: \(user.id)")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                    .padding(.vertical, 4)
                }
                .refreshable {
                    viewModel.loadUsers()
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(UIColor.systemBackground))
        .onAppear {
            // Load users when the view appears if not already loaded
            if viewModel.users.isEmpty {
                viewModel.loadUsers()
            }
        }
    }
}
