# Mastermind Game

Mastermind Game is a word puzzle game for Android. It is a high-end pilot version designed to demonstrate modern Android development practices while providing an engaging puzzle experience.

## 🚀 Architecture & Tech Stack

The project is built following **Clean Architecture** principles and the **MVVM** (Model-View-ViewModel) pattern to ensure scalability, maintainability, and testability.

### Technologies Used:
*   **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern declarative UI toolkit.
*   **Language**: [Kotlin 2](https://kotlinlang.org/) - Utilizing the latest compiler and language features.
*   **Async & Data**: 
    *   Kotlin Coroutines & Flow - For asynchronous programming and reactive data streams.
    *   [Room Database](https://developer.android.com/training/data-storage/room) - Local data persistence for game results.
*   **Dependency Injection**: [Koin](https://insert-koin.io/) - Pragmatic lightweight dependency injection.
*   **Libraries**:
    *   Android Jetpack Libraries (Navigation, ViewModel, Lifecycle).
    *   Timber - For structured logging (disabled in production).
*   **Testing**:
    *   [JUnit 5 & Jupiter](https://junit.org/junit5/) - Comprehensive unit testing framework.
    *   MockK - For mocking dependencies in tests.
    *   Turbine - For testing Kotlin Flows.

## 🎮 How to Play

1.  **Main Menu**: Upon launching, you are presented with a basic Main Menu featuring **New Game**, **Hall Of Fame**, and **Quit** buttons.
2.  **Start Game**: Pressing "New Game" redirects you to the Mastermind Screen.
3.  **Gameplay**: 
    *   Your goal is to guess a **Secret 4-letter word**.
    *   A brief hint is provided to guide your search.
    *   Enter your guess and check the feedback colors to narrow down the answer.
4.  **Victory**: If you guess correctly, a dialog appears for you to enter your name. Your score is saved, and you are taken to the **Victory Screen**.
5.  **Game Over**: If the timer runs out before you find the word, the **Game Over Screen** is displayed.
6.  **Hall Of Fame**: Access this section from the main menu to view the **Top 10** local high scores.

## 🧪 Testing Strategy

The project includes a robust testing suite covering both business logic and user interface components.

### Unit Tests (JUnit 5 & MockK)
Located in `app/src/test/java`, these tests ensure the core logic is sound:
*   **Game Logic**: Verifies the "Mastermind" algorithm, checking for correct letters, incorrect positions, and handling of duplicate letters.
*   **ViewModel**: Tests the state management, including timer behavior (countdown and expiration), score calculation, and proper interaction with Use Cases.
*   **Coroutines**: Uses a custom `CoroutineTestExtension` to handle main thread dispatching and ensure stable, non-flaky async tests.

### UI Tests (Compose Test Rule)
Located in `app/src/androidTest/java`, these tests verify the visual integrity and user flow:
*   **Navigation**: Ensures all buttons on the Main Menu correctly navigate to their respective screens.
*   **Component Display**: Verifies that critical UI elements like "Game Over" text and "Hall of Fame" titles are visible when expected.
*   **User Input**: Simulates typing letters into the guess fields to ensure the UI updates and reflects the user's input correctly.
*   **Interaction**: Tests button click callbacks to ensure the app responds correctly to user actions.

---

> 💡 **IMPORTANT (Testing Tip)**: In the **Debug build**, the secret answer is conveniently hidden in the **Top Left corner** of the screen to allow for rapid testing and verification of game logic.
