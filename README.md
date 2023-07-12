# Marvel Comics App

The Marvel Comics App is an Android application that utilizes the Marvel API (https://developer.marvel.com/) to provide users with access to a wide range of comics information. It offers a user-friendly interface and incorporates various features for easy searching and browsing as well as saving favourite comics.

## Architecture

The app is developed using Kotlin and follows the MVVM (Model-View-ViewModel) and Dependency Injection (using Hilt library) design patterns, ensuring a robust and maintainable codebase. It leverages LiveData to facilitate seamless data updates. For handling asynchronous tasks, coroutines are employed, enabling efficient background processing.

## Key Features

The Marvel Comics App boasts the following noteworthy features:

- Comic Card View: The app displays comics using an adaptive height approach that adjusts to the proportions of each image, providing an aesthetically pleasing viewing experience.

- Custom Slideup Card: By utilizing MotionLayout and MotionScene, the app implements a visually appealing slideup card effect for revealing detail information.

- Comprehensive Searching: Users can easily search for specific comics, and the app provides feedback if the search yields no results.

- Bottom Navigation Menu: The app incorporates a bottom navigation menu that dynamically changes icon colors when selected. This effect is achieved using a selector.

- Light and Dark Themes: The app supports both light and dark themes, allowing users to personalize their viewing experience.

- Responsive Design: The app is designed to work seamlessly in both portrait and horizontal modes, as well as on tablets, providing a consistent and adaptable user interface across various device orientations and sizes.

- Firebase integration: The app supports firebase for saving comics online.

## Third-Party Libraries

Marvel Comics App integrates the following third-party libraries:

- Retrofit and Converter-Gson: Communication with the Marvel API.

- Glide: Loading and displaying images from URLs.

- Firebase: Authentication and online data storage.

- Hilt: Dependecy Injection

## Gallery
<p align="center">
  <img src="https://github.com/KotekKacper/Marvel_Comics_App/assets/71709842/3ff2951c-365a-430a-8303-89530af62e59" alt="MC-home" width="250">
  &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/KotekKacper/Marvel_Comics_App/assets/71709842/63e36c97-04f4-4980-96e7-3beca8d458fe" alt="MC-search" width="250">
  &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/KotekKacper/Marvel_Comics_App/assets/71709842/fecd23ce-980c-4608-8020-f012889b428c" alt="MC-saved" width="250">
</p>

<p align="center">
  &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/KotekKacper/Marvel_Comics_App/assets/71709842/40bd0dbc-77fa-45ee-ba59-f5ada760204d" alt="MC-responsive" width="500">
  &nbsp;&nbsp;&nbsp;
</p>

