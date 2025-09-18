import SwiftUI

struct Screen1View: View {
    var body: some View {
        VStack {
            Spacer()
            Text("Screen 1")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.primary)
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(UIColor.systemBackground))
    }
}