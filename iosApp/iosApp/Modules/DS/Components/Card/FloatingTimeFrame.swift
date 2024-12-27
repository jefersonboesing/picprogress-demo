    //
    //  FloatingTimeFrame.swift
    //  iosApp
    //
    //  Created by Jeferson on 06/07/23.
    //  Copyright © 2023 orgName. All rights reserved.
    //

    import SwiftUI
    import shared

//    struct FloatingTimeFrame: View {
//        let text: String
//        let isCurrent: Bool
//        let isCompleted: Bool
//        let colors: AlbumTheme
//        
//        @State private var backgroundHeight: CGFloat = 12
//        @State private var textColorAlpha: CGFloat = 0
//        
//        var body: some View {
//            let maxHeight: CGFloat = 44
//            let minHeight: CGFloat = 12
//            let textColor = isCompleted && isCurrent ? colors.asSecondaryTextColor() : colors.asPrimaryTextColor()
//            let backgroundColor = isCompleted ? colors.asSecondaryColor() : Color(UIColor(resource: .cardBoxDefault))
//            
//            return ZStack(alignment: .bottom) {
//                RoundedRectangle(cornerRadius: 8)
//                    .fill(backgroundColor)
//                    .frame(maxWidth: .infinity, maxHeight: backgroundHeight)
//                    .clipped()
//                
//                TimeFrameLabel(
//                    text: text,
//                    color: textColor.opacity(textColorAlpha)
//                )
//                .frame(maxHeight: maxHeight)
//            }
//            .onAppear {
//                let targetHeight = isCurrent ? maxHeight : minHeight
//                withAnimation(.easeInOut(duration: 0.8)) {
//                    backgroundHeight = targetHeight
//                    textColorAlpha = 1
//                }
//            }
//        }
//    }
//
//
//    struct FloatingTimeFrame_Previews: PreviewProvider {
//        static var previews: some View {
//            ZStack(alignment: .center) {
//                FloatingTimeFrame(
//                    text: "Apr",
//                    isCurrent: true,
//                    isCompleted: true,
//                    colors: AlbumTheme.Emerald()
//                )
//            }
//            
//        }
//    }


struct FloatingTimeFrame: View {
    var text: String
    var isCurrent: Bool
    var isCompleted: Bool
    var theme: AlbumTheme
    var textAlignment: Alignment = .top
    var animate: Bool

    @State private var backgroundHeight: CGFloat = 12
    @State private var textColorAlpha: Double = 1

    var body: some View {
        let maxHeight: CGFloat = 44
        let minHeight: CGFloat = 12
        let textColor = (isCompleted && isCurrent) ? theme.asSecondaryTextColor() : theme.asPrimaryTextColor()
        let backgroundColor = (isCompleted) ? theme.asSecondaryColor() : theme.asPrimaryTextColor().opacity(0.1)

        ZStack(alignment: .bottom) {
            Rectangle()
                .fill(backgroundColor)
                .frame(maxHeight: backgroundHeight)
                .cornerRadius(8)
            Text(text)
                .font(.labelSmall())
                .foregroundColor(textColor.opacity(textColorAlpha))
                .multilineTextAlignment(.center)
                .padding(.top, 13)
                .frame(maxWidth: .infinity, maxHeight: maxHeight, alignment: textAlignment)
        }
        .frame(height: maxHeight)
        .onAppear {
            withAnimation(animate ? .spring(duration: 0.8) : nil) {
                self.backgroundHeight = isCurrent ? maxHeight : minHeight
                self.textColorAlpha = 1
            }
        }
    }
}


struct FloatingTimeFrame_Previews: PreviewProvider {
    static var previews: some View {
        ZStack(alignment: .center) {
            FloatingTimeFrame(
                text: "Apr",
                isCurrent: true,
                isCompleted: true,
                theme: AlbumTheme.Emerald(),
                animate: true
            )
        }
        
    }
}
