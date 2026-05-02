**Offline News Reader**

A modern Android news app built with a focus on offline-first architecture, clean design, and real-world scalability.

This project was developed as part of an Android assessment to demonstrate how production-level apps handle network + local data + reactive UI.


**What this app does**
-Displays latest news articles
-Supports offline viewing
-Allows bookmarking articles
-Provides instant search
-Updates data with pull-to-refresh
-Handles errors and loading states gracefully

**Key Highlights**

This app is not just about UI — it focuses on how data flows in a real app.

-->_Offline-first approach_
    -Data is always loaded from the local database first, then synced with network.
-->_Single source of truth_
   -Room database drives the UI using Flow.
-->_Reactive UI_
   -UI updates automatically when data changes.
-->_Search from database_
    -Works even without internet.
-->_Mocked API with Retrofit_
   -Uses a local JSON via interceptor to simulate real network responses.
-->Paging 3 with RemoteMediator
Production-grade pagination with Room DB as source of truth.
Bookmark state is preserved across page refreshes.
Proper load states and retry logic handled.

-->Live Connectivity Observer
Built using callbackFlow — detects network loss in real time.
Shows offline banner instantly with cached content fallback.

-->Unit Tested ViewModel
5 test cases written using MockK + Turbine.
Covers initial load, search, offline state, refresh, and bookmark toggle.

**Architecture**

The project follows a Clean Architecture (lightweight) approach:

UI (Compose)
   ↓
ViewModel
   ↓
UseCases
   ↓
Repository
   ↓
Room Database ←→ Retrofit (Mock JSON)

**Offline-First Flow**
1.Load cached data from Room
2.Show it instantly on UI
3.Fetch latest data (mocked JSON)
4.Update database
5.UI updates automatically

**Search**
-Implemented using Room queries
-Uses Flow + debounce + flatMapLatest
-Works completely offline
Pagination
- Implemented using Paging 3 with RemoteMediator
- Room acts as single source of truth
- Handles append, refresh, and error load states
- Bookmark state preserved across refreshes
- Retry logic built in for failed pages

**Network Monitoring**
- Uses callbackFlow to observe connectivity in real time
- Offline banner shown automatically when network is lost
- Cached content always available offline

**Testing**
- ViewModel unit tested with JUnit4 + MockK + Turbine
- Test cases: initial load, search trigger, offline state detection,
  refresh, and bookmark toggle

**Features**
-Bookmark articles (persisted locally)
-Pull-to-refresh support
-Error handling with retry option
-Loading states for better UX
-Clean and scalable code structure
-Paging 3 with RemoteMediator for scalable pagination
-Bookmark state preserved across page refreshes
-Live offline banner using callbackFlow network observer
-Shimmer loading animation while content loads
-Share article via any app (WhatsApp, Gmail, Telegram)
-Tap article to open full content in browser
-Retry option on failed page loads
-Unit tested ViewModel with MockK + Turbine

**Tech Stack**
- Language: Kotlin
- UI: Jetpack Compose
- Architecture: MVVM + Clean Architecture
- Pagination: Paging 3 + RemoteMediator
- Local DB: Room
- Networking: Retrofit + OkHttp (Mock Interceptor)
- DI: Koin
- Async: Coroutines + Flow + StateFlow + callbackFlow
- Testing: JUnit4 + MockK + Turbine

Screenshots:
<img width="1080" height="2424" alt="56826" src="https://github.com/user-attachments/assets/670e76ff-de9a-42f0-a955-5f8abb0afb14" />
<img width="1080" height="2424" alt="56828" src="https://github.com/user-attachments/assets/84c0b386-79bb-4d38-a21d-3b9ad8f942e5" />

