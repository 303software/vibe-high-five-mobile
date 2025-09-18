import SwiftUI

struct LeaderboardView: View {
    var body: some View {
        VStack {
            Spacer()
            Text("Leaderboard")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.primary)
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(UIColor.systemBackground))
    }
}
