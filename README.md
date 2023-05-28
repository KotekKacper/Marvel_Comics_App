# Marvel Comics App

The Marvel Comics App is an Android application that utilizes the Marvel API (https://developer.marvel.com/) to provide users with access to a wide range of comics information. It offers a user-friendly interface and incorporates various features for easy searching and browsing.

## Architecture

The app is developed using Kotlin and follows the MVVM (Model-View-ViewModel) design pattern, ensuring a robust and maintainable codebase. It leverages LiveData to facilitate seamless data updates. For handling asynchronous tasks, coroutines are employed, enabling efficient background processing.

## Key Features

The Marvel Comics App boasts the following noteworthy features:

- Comic Card View: The app displays comics using an adaptive height approach that adjusts to the proportions of each image, providing an aesthetically pleasing viewing experience.

- Custom Slideup Card: By utilizing MotionLayout and MotionScene, the app implements a visually appealing slideup card effect for revealing detail information.

- Comprehensive Searching: Users can easily search for specific comics, and the app provides feedback if the search yields no results.

- Bottom Navigation Menu: The app incorporates a bottom navigation menu that dynamically changes icon colors when selected. This effect is achieved using a selector.

- Light and Dark Themes: The app supports both light and dark themes, allowing users to personalize their viewing experience.

- Responsive Design: The app is designed to work seamlessly in both portrait and horizontal modes, as well as on tablets, providing a consistent and adaptable user interface across various device orientations and sizes.

## Third-Party Libraries

Marvel Comics App integrates the following third-party libraries:

- Retrofit (version 2.9.0) and Converter-Gson (version 2.9.0): Communication with the Marvel API.

- Glide (version 4.12.0): Loading and displaying images from URLs.

## Gallery
<p align="center">
  <img src="https://github.com/KotekKacper/Marvel_Comics_App/assets/71709842/b85bcaad-1162-4ef8-b5a0-2377b3cfd3d2" alt="MC-home" width="250">
  &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/KotekKacper/Marvel_Comics_App/assets/71709842/ea0fec5c-2cfe-423f-a227-a8e2d7d70e3c" alt="MC-search" width="250">
  &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/KotekKacper/Marvel_Comics_App/assets/71709842/e8760783-212c-488b-b239-0f71f6ea9cca" alt="MC-dark" width="250">
</p>

<p align="center">
  &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/KotekKacper/Marvel_Comics_App/assets/71709842/75c2c7fa-2d26-44a1-8a10-06f059dcf32f" alt="MC-responsive" width="500">
  &nbsp;&nbsp;&nbsp;
</p>
