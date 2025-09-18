import SwiftUI

struct UsersView: View {
    var body: some View {
        VStack {
            Spacer()
            Text("Users")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.primary)
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(UIColor.systemBackground))
    }
}
