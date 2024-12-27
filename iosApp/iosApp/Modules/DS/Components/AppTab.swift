//
//  AppTab.swift
//  PicProgress
//
//  Created by Jeferson on 26/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AppTab: View {
    let tabs: [AppIconTab]
    let selectedIndex: Int
    let onTabClick: (Int) -> Void
    
    @State private var positions: [CGFloat]
    @State private var indicatorXOffset: CGFloat
    
    init(tabs: [AppIconTab], selectedIndex: Int, onTabClick: @escaping (Int) -> Void) {
        self.tabs = tabs
        self.selectedIndex = selectedIndex
        self.onTabClick = onTabClick
        self.positions = .init(repeating: .zero, count: tabs.count)
        self.indicatorXOffset = 0
    }
    
    var body: some View {
        let appTabSpace = "AppTab"
        ZStack(alignment: .leading) {
            RoundedRectangle(cornerRadius: 48)
                .fill(.custom(.PrimaryMedium()))
                .frame(width: 64, height: 40)
                .offset(x: indicatorXOffset, y: 0)
            
            HStack(alignment: .center) {
                ForEach(Array(tabs.enumerated()), id: \.offset) { index, tab in
                    Image(tab.icon)
                        .renderingMode(.template)
                        .frame(width: 24, height: 24)
                        .aspectRatio(contentMode: .fit)
                        .foregroundColor(
                            index == selectedIndex ? .custom(.PrimaryLightest()) : .custom(.PrimaryMedium())
                        )
                        .padding(.horizontal, 20)
                        .padding(.vertical, 8)
                        .background(
                            GeometryReader { geometry in
                                Color.clear
                                    .onAppear {
                                        positions[index] = geometry.frame(in: .named(appTabSpace)).minX
                                    }
                            }
                        )
                        .clipShape(RoundedRectangle(cornerRadius: 48))
                        .onTapGesture {
                            onTabClick(index)
                            withAnimation(.easeInOut(duration: 0.3)) {
                                indicatorXOffset = positions[index]
                            }
                        }
                }
            }.coordinateSpace(name: appTabSpace)
        }
        .padding(4)
        .background(.custom(.PrimaryLightest()))
        .cornerRadius(48)
        .frame(height: 48)
    }
}

struct AppIconTab {
    var icon: ImageResource
}


