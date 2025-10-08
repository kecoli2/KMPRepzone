import SwiftUI
import Mobile

@main
struct iOSApp: App {
    init() {
        Mobile.KoinKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
