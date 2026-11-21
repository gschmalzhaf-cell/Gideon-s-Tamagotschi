# Gideon – dein Tamagot(s)chi 🐣

**Tamagot(s)chi** – ja, mit Klammer-s. Weil Gideon manchmal eins braucht und manchmal keins.
Gideon ist dein digitales Haustier für Android: füttere es, spiele mit ihm, leg es schlafen
und halte es sauber. Vernachlässigst du es, wird es bockig. Sehr bockig. Und bei Gesundheit 0:
Game Over. Bei guter Pflege dagegen entwickelt es sich vom Baby zum Erwachsenen.

## Die Legende von Gideon

Es war einmal ein Entwickler, der eines Morgens in seiner Hosentasche ein leises *Piep* hörte.
Dort saß Gideon – ein kleiner, pinker Pixelhaufen mit zwei Augen. „Fütter mich“ sagte er nicht,
aber seine Augen sagten es. Seitdem wohnt Gideon auf seinem Android-Telefon. Er isst nicht
viel (nur alle paar Minuten ein bisschen Aufmerksamkeit), aber wenn du ihn vergisst, merkt
er sich das. Den Rest vergisst er sofort wieder. Ein wahres Tamagot(s)chi eben.

## Features

- 🍔 **Füttern**, 🎾 **Spielen**, 🛁 **Waschen**, 😴 **Schlafen legen**, 💊 **Medizin geben**
- 📊 Lebensbalken: Hunger, Glück, Energie, Sauberkeit, Gesundheit
- 👶 → 🧒 → 🧑 → 🧔 Lebenszyklus: Baby, Kind, Teenager, Erwachsener (Evolution nach Alter & Pflegequalität)
- 🌙 lebt im Hintergrund weiter (WorkManager) – inkl. „Während du weg warst …“-Dialog beim Öffnen
- 🔔 Benachrichtigungen: „hat Hunger!“, „geht es schlecht“, „vermisst dich“
- ⚙️ Einstellungen: Benachrichtigungen & Sounds an/aus (DataStore)
- 🎨 Farbe & Typ frei wählbar beim Anlegen
- 💀 Game Over bei Gesundheit 0 – mit Neustart für ein neues Pet
- ✨ Lebendige Animation (Puls; im Schlaf langsamer, bei Tod grau)

## Download

Signierte Releases (APK & AAB) gibt es unter
[Releases](https://github.com/gschmalzhaf-cell/Gideon-s-Tamagotschi/releases) – getriggert durch Tags `v*`.

## Tech-Stack

Kotlin + Jetpack Compose (Material 3), MVVM, Hilt, Room, DataStore, WorkManager, Navigation Compose

## Für Entwickler

Voraussetzungen: JDK 17 und Android SDK (compileSdk 35).

Das Gradle-Wrapper-Jar ist nicht eingecheckt; einmalig generieren mit:

```
gradle wrapper --gradle-version 8.10.2
```

Danach:

```
./gradlew assembleDebug     # Debug-APK
./gradlew testDebugUnitTest # Unit-Tests (Game-Logik)
```

- **CI**: GitHub Actions auf jedem PR (`.github/workflows/ci.yml`: `assembleDebug` + `testDebugUnitTest`)
- **Release**: `.github/workflows/release.yml` auf Tag `v*` – signiertes APK/AAB via GitHub Secrets, GitHub Release automatisch

### Phasen

1. Projektgerüst (Gradle, Hilt, Navigation, Theme)
2. Pet erstellen + Home-Screen mit Statusbalken + Room
3. Aktionen + Foreground-Game-Tick
4. WorkManager-Background-Ticks, Offline-Sync, Lebenszyklus/Evolution, GameOver
5. Benachrichtigungen, DataStore-Einstellungen, Sounds, Animationen
6. Unit-Tests + Release-Config (Signing, ProGuard, Release-Workflow) – aktueller Stand
