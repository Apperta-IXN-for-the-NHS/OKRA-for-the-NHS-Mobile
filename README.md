# OKRA Mobile

An Android mobile application for the Open Knowledge Rank (OKRA).

## Build project
To build the project run the following terminal command from the root directory of the project:
`./gradlew clean build`

## Run project
To run the project you need to use an emulator or a physical Android device. Follow this official
Android Developers guide: https://developer.android.com/training/basics/firstapp/running-app

## Run tests
Ton run all unit tests run the following terminal command from the root directory of the project:
`./gradlew test`

Ton run all instrumented unit tests run the following terminal command from the root directory of 
the project:
`./gradlew connectedAndroidTest`

## Setup connection to chat
Currently, when clicking on the "Chat with us" button in the "Contact Us" tab, a new web page is
opened in a WebView within the app. The URL to the external chat can be changed in the `strings.xml` 
file in the `/res/values` folder.

## Setup connection to backend
1. In the `strings.xml` file in the `/res/values` folder change the value for the `backendUrl` field.
2. In the `network_security_config.xml` file in the `/res/xml` folder change the configuration 
accordingly.

## Setup contact details
In the `strings.xml` file in the `/res/values` folder you will find configurations for different
address fields. These are used to display contact information in the "Contact Us" tab. Change them 
according to your needs. 


# How to contribute
When contributing to the project, make sure you do the following before opening a pull request:
1. In the application's `build.gradle` file bump the `versionCode` by 1, e.g. from 15 to 16
2. In the same `build.gradle` file change the `versionName` by following 
[Semantic Versioning](https://semver.org/spec/v2.0.0.html). When submitting a bugfix, change the
third number. When submitting a minor change, change the second number. When submitting a major 
change - the first.
3. List and explain the changes in the `CHANGELOG.md` file in the root directory. Follow the 
existing structure, and make sure you add the version name and date.
