import SwiftUI
import shared
import Foundation

@main
struct iOSApp: App {
    
    @Environment(\.scenePhase) private var scenePhase
    
    private let appNavigation = AppNavigation()
    
    init() {
        Injection().initialize()
    }
    
    var body: some Scene {
        WindowGroup {
            AppView().environmentObject(appNavigation)
        }
    }
    
}

extension UINavigationController: UIGestureRecognizerDelegate {
    override open func viewDidLoad() {
        super.viewDidLoad()
        interactivePopGestureRecognizer?.delegate = self
    }
    
    public func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
        return viewControllers.count > 1
    }
}
