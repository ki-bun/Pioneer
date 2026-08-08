# Pioneer

**An open source tool to keep track of progress.**

Whether you are reading a book, PDF, EPUB, watching playlists, anime episodes, manga chapters, or anything else, this application will act as one inventory to manage them, without relying on several different services for each.

This project was made primarily to **support my learning process**, because I needed to keep track of the learning resources I was learning from. 

## Download 

You can get the latest version from [Github Releases](https://github.com/ki-bun/Pioneer/releases) or from the following app stores:

- [F-Droid](https://f-droid.org/packages/com.ki_bun.pioneer)
- [IzzyOnDroid](https://apt.izzysoft.de/fdroid/index/apk/com.ki_bun.pioneer)

## Features

- **Material 3 dynamic theme**
- **Local** and **offline** progress tracking, no accounts or internet connection required
- Adding, deleting, archiving, and editing progress
- **Privacy focused** progress tracking app with no data collection, analytics, and telemetery
- **Import** and **export** to CSV
- Categorize into **tags**
- Display **images** for better visibility
- Supports **highlighting links** in description
- Supports adding **application shortcuts**
- **Open source** and **ad-free**

## Screenshots

<table>
	<tr>
		<td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/screenshot_1.png" alt="screenshot_1"></td>
		<td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/screenshot_2.png" alt="screenshot_2"></td>
		<td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/screenshot_3.png" alt="screenshot_3"></td>
		<td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/screenshot_4.png" alt="screenshot_4"></td>
	</tr>
</table>

## Build the app

1. Clone this repository
2. Run `./gradlew assembleDebug` or open it in Android Studio and run it from there

### Building a signed release

1. Make a file named `secrets.properties` and add values to the following:
```
PIONEER_STORE_PASSWORD=
PIONEER_KEY_PASSWORD=
PIONEER_KEY_ALIAS=
```
2. Run `./gradlew assembleRelease`

## License
Pioneer is a free and open source project licensed under the **GNU General Public License v3.0**
