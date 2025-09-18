import SwiftUI

struct AppToolbar: View {
    let title: String
    let onHamburgerTapped: () -> Void
    
    var body: some View {
        HStack {
            Button(action: onHamburgerTapped) {
                Image(systemName: "line.horizontal.3")
                    .font(.title2)
                    .foregroundColor(.primary)
            }
            .padding(.leading, 16)
            
            Spacer()
            
            Text(title)
                .font(.headline)
                .fontWeight(.semibold)
                .foregroundColor(.primary)
            
            Spacer()
            
            // Invisible spacer to balance the hamburger button
            Image(systemName: "line.horizontal.3")
                .font(.title2)
                .opacity(0)
                .padding(.trailing, 16)
        }
        .frame(height: 44)
        .background(Color(UIColor.systemBackground))
        .overlay(
            Rectangle()
                .frame(height: 0.5)
                .foregroundColor(Color(UIColor.separator)),
            alignment: .bottom
        )
    }
}