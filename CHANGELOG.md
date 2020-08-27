# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.19.0] - 2020-08-27

### Added
- Better HTTP request logging
- Callback interface for handling responses outside of REST module
- Post votes to the backend

## [0.18.0] - 2020-08-27

### Added
- Show article score and views

## [0.17.0] - 2020-08-26

### Added
- Create case functionality

## [0.16.0] - 2020-08-25

### Added
- Case listings from API
- Display case functionality

## [0.15.0] - 2020-08-22

### Added
- Refactor vote services to use dependency injection to facilitate testing
- Unit tests for said services

## [0.14.0] - 2020-08-22

### Added
- Loading dialog and web client for loading web pages

## [0.13.3] - 2020-08-21

### Fixed
- Article vote buttons text and color

## [0.13.2] - 2020-08-21

### Fixed
- Chatbot not displayed properly in the web view

## [0.13.1] - 2020-08-21

### Fixed
- Issue that searching was being done on scroll even without button click

## [0.13.0] - 2020-08-20

### Added
- Search functionality

## [0.12.0] - 2020-08-20

### Added
- Persisting article votes locally on the mobile device
- Logging of HTTP requests and responses

## [0.11.0] - 2020-08-19

### Fixed
- Article view Styled

### Added
- Recommended articles as cards in article view

## [0.10.0] - 2020-08-18

### Added
- Card views in knowledge base

## [0.9.1] - 2020-08-17

### Fixed
- Bug where new articles were not being displayed when scrolling

## [0.9.0] - 2020-08-17

### Added
- Styled the Contact Us tab
- Remove the top bar

## [0.8.1] - 2020-08-17

### Fixed
- Back button in the DisplayArticle page to keep the same position of the list

## [0.8.0] - 2020-08-16

### Added
- Load articles dynamically with scrolling


## [0.7.0] - 2020-08-16

### Added
- Repository module for REST calls
- ViewModels for persisting UI state

### Fixed
- Scrolling the text of articles
- NPE when an article has an empty body


## [0.6.0] - 2020-08-12

### Added
- API integration for Knowledge module
- DisplayArticle activity


## [0.5.0] - 2020-08-06

### Added
- App launcher icon and colors complying with EMIS' image and brand

## [0.4.0] - 2020-08-04

### Added
- Bottom navigation with fragments instead of activities


## [0.3.0] - 2020-07-28

### Added
- Empty WebView with example.com
- Floating button opening the phone app of the mobile device with a configured phone number


## [0.2.0] - 2020-07-26

### Added
- Knowledge and Contact Us activities
- Navigation from the Main activity


## [0.1.0] - 2020-07-25

### Configured
- Configure Gradle scripts
- Configure Travis CI
