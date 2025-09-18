import SwiftUI

struct SlideOutMenu: View {
    let onScreen1Tapped: () -> Void
    let onScreen2Tapped: () -> Void
    let onLogoutTapped: () -> Void
    let onClose: () -> Void
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            VStack(alignment: .leading, spacing: 24) {
                Button(action: onScreen1Tapped) {
                    HStack {
                        Text("Screen 1")
                            .font(.body)
                            .foregroundColor(.primary)
                        Spacer()
                    }
                    .padding(.vertical, 12)
                    .padding(.horizontal, 20)
                    .background(Color.clear)
                }
                
                Button(action: onScreen2Tapped) {
                    HStack {
                        Text("Screen 2")
                            .font(.body)
                            .foregroundColor(.primary)
                        Spacer()
                    }
                    .padding(.vertical, 12)
                    .padding(.horizontal, 20)
                    .background(Color.clear)
                }
                
                Button(action: onLogoutTapped) {
                    HStack {
                        Text("Log Out")
                            .font(.body)
                            .foregroundColor(.red)
                        Spacer()
                    }
                    .padding(.vertical, 12)
                    .padding(.horizontal, 20)
                    .background(Color.clear)
                }
            }
            .padding(.top, 60) // Space for status bar and toolbar
            
            Spacer()
        }
        .frame(width: 250)
        .background(Color(UIColor.systemBackground))
        .overlay(
            Rectangle()
                .frame(width: 0.5)
                .foregroundColor(Color(UIColor.separator)),
            alignment: .trailing
        )
        .shadow(radius: 5)
    }
}