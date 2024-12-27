import SwiftUI
import shared

struct CircularTimeFrame: View {
    let text: String
    let isCompleted: Bool
    let colors: AlbumTheme
    
    var body: some View {
        let boxHeight: CGFloat = text.isEmpty ? 12 : 20
        ZStack(alignment: .bottom) {
            Rectangle()
                .fill(isCompleted ? colors.asSecondaryColor() : Int64(0x1A000000).asColor())
                .frame(maxHeight: boxHeight)
                .cornerRadius(100)
            
            Text(text)
                .font(.labelSmall())
                .foregroundColor(isCompleted ? colors.asSecondaryTextColor() : colors.asPrimaryTextColor())
                .multilineTextAlignment(.center)
                .frame(maxWidth: .infinity, minHeight: 20)
        }
        
    }
}

struct CircularTimeFrame_Previews: PreviewProvider {
    static var previews: some View {
        CircularTimeFrame(
            text: "Apr",
            isCompleted: true,
            colors: AlbumTheme.Plum()
        )
    }
}
