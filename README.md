# Gideon's – Tamagot(s)chi

Gideon ist eine Android-App im Tamagotchi-Stil: ein digitales Haustier, das du
füttern, mit dem du spielen, das du schlafen legen und pflegen musst. Wird es
vernachlässigt, stirbt es – bei guter Pflege entwickelt es sich vom Baby zum
Erwachsenen.

## Tech-Stack

- Kotlin + Jetpack Compose (Material 3), MVVM
- Hilt (Dependency Injection), Navigation Compose
- Room, DataStore, WorkManager (ab den späteren Phasen)

## Setup

Voraussetzungen: JDK 17 und Android SDK (compileSdk 35).

Das Gradle-Wrapper-Jar ist nicht eingecheckt; einmalig generieren mit:

```
gradle wrapper --gradle-version 8.10.2
```

Danach build mit:

```
./gradlew assembleDebug
```

Alternativ lässt sich das Projekt direkt in Android Studio öffnen.

## Phasen

1. Projektgerüst (Gradle, Hilt, Navigation, Theme) – aktueller Stand
2. Pet erstellen + Home-Screen mit Statusbalken
3. Aktionen + Game-Tick
4. Background-Ticks, Offline-Sync, Lebenszyklus/Tod
5. Benachrichtigungen, Animationen, Sounds
6. Tests + Release-Vorbereitung
