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

**Features**
-Bookmark articles (persisted locally)
-Pull-to-refresh support
-Error handling with retry option
-Loading states for better UX
-Clean and scalable code structure

Screenshots:
<img width="1080" height="2424" alt="56826" src="https://github.com/user-attachments/assets/670e76ff-de9a-42f0-a955-5f8abb0afb14" />
<img width="1080" height="2424" alt="56828" src="https://github.com/user-attachments/assets/84c0b386-79bb-4d38-a21d-3b9ad8f942e5" />

