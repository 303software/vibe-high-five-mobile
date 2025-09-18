//
//  UpdateRequiredView.swift
//  iosApp
//
//  Created by Scott Quinney on 9/17/25.
//  Copyright © 2025 orgName. All rights reserved.
//


import SwiftUI

struct UpdateRequiredView: View {
    var body: some View {
        VStack {
            Spacer()
            Text("Update Required")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.primary)
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(UIColor.systemBackground))
    }
}
