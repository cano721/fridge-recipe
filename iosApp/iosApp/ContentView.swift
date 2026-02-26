import SwiftUI

struct ContentView: View {
    var body: some View {
        VStack {
            Text("냉장고 레시피")
                .font(.largeTitle)
                .fontWeight(.bold)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

#Preview {
    ContentView()
}
