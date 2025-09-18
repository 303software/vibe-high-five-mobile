import SwiftUI
import shared

@main
struct iOSApp: App {
    static let rootViewModel = RootViewModel()
    static let envStr = (Bundle.main.infoDictionary?["LSEnvironment"] as! Dictionary<String, String>)["RELEASE_ENV"]!
    
    static var baseUrl: String {
        switch envStr {
        case "PROD":
            return "https://dummyjson.com/"
        case "UAT":
            return "https://dummyjson.com/"
        default:
            return "https://dummyjson.com/"
        }
    }

    static var networkClient = NetworkClient()
    static let store = LocalStore()
    static var apiRefreshToken: String {
        store.getString(key: NetworkClient.companion.REFRESH_TOKEN) ?? ""
    }

	var body: some Scene {
		WindowGroup {
			RootView()
		}
	}
}
