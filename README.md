# TodoApp - TODO List Application

This application is a TODO list app built using Kotlin and Jetpack Compose, following Clean Architecture principles and utilizing Hilt for dependency injection.

## Features

* Display a list of TODO items on the main screen.
* Show an empty state message when the list is empty.
* Search functionality to filter the TODO list (with a 2-second debounce).
* Add new TODO items via a details screen.
* Store TODO items in a local Room database.
* Handle errors, including a specific error for "Error" input.
* Maintain state across screen rotations.
* Unit tests for core components.
* Modular project structure.

## Architecture

* **Clean Architecture:** The app follows Clean Architecture principles, separating concerns into presentation, domain, and data layers.
* **MVVM:** The presentation layer uses the Model-View-ViewModel (MVVM) pattern.
* **Hilt:** Hilt is used for dependency injection.
* **Kotlin Flows and Coroutines:** Asynchronous operations are handled using Kotlin Flows and Coroutines.
* **Room Persistence Library:** A local Room database is used for data storage.
* **Jetpack Compose:** The UI is built using Jetpack Compose.
* **Modularization:** The project is modularized for better organization and maintainability.

## Modules

* **`app`:** Application entry point, navigation, and dependency injection setup.
* **`core`:** Reusable components and utilities.
* **`data`:** Data access layer (Room database, repositories).
* **`domain`:** Business logic and use cases.
* **`home`:** TODO list and Add TODO item feature.

## Git Commit Guidelines

To maintain a clean and consistent commit history, please follow these guidelines:

* **Use descriptive commit messages:** Clearly explain the purpose of each commit.
* **Keep commits small and focused:** Each commit should address a single logical change.
* **Follow a consistent commit message format:**
    * **`feat:`** for new features.
    * **`fix:`** for bug fixes.
    * **`docs:`** for documentation changes.
    * **`style:`** for code style changes.
    * **`refactor:`** for code refactoring.
    * **`test:`** for test-related changes.
    * **`chore:`** for general maintenance tasks.

**Example Commit Messages:**

* `feat: Implement search functionality with 2-second debounce`
* `fix: Handle "Error" input in AddTodoScreen`
* `refactor: Modularize project structure`
* `test: Add unit tests for MainViewModel`

## Building the Project

1.  Clone the repository.
2.  Open the project in Android Studio.
3.  Build and run the application.

## Unit Tests

To run the unit tests, use the following command in Android Studio:
./gradlew testDebugUnitTest

## Dependencies

* Jetpack Compose
* Room Persistence Library
* Hilt
* Kotlin Coroutines
* JUnit
* Mockito
* Turbine

## Author

* Govi
