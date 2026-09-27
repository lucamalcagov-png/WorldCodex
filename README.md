# WorldCodex

Android app for organizing a fictional universe.

## Build

Recommended: Android Studio with Android SDK Platform 35 and Build Tools installed.

1. Open the `WorldCodex` folder in Android Studio.
2. Let Gradle sync.
3. Choose **Build > Build APK(s)**.
4. APK: `app/build/outputs/apk/debug/app-debug.apk`

Command line (after installing Gradle 8.9+):
- Linux/macOS: `./gradlew assembleDebug`
- Windows: `gradlew.bat assembleDebug`

The project uses Android Gradle Plugin 8.7.3 and Gradle 8.9.


## Облачная сборка APK

Проект содержит GitHub Actions workflow: `.github/workflows/build-apk.yml`.

После загрузки проекта в GitHub:
1. Откройте вкладку **Actions**.
2. Выберите **Build World Codex APK**.
3. Нажмите **Run workflow**.
4. После завершения откройте результат запуска и скачайте artifact **WorldCodex-debug-apk**.

Workflow собирает `app-debug.apk` на GitHub-hosted Ubuntu runner и сохраняет APK как workflow artifact.
