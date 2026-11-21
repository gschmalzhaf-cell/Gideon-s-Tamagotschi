<div align="center">

# 🐣 Gideon's Tamagot(s)chi

### *Dein retro-pastellfarbenes Taschenhaustier für Android – mit Herz, Eigenleben & Klammer-s.*

[![Release](https://img.shields.io/github/v/release/gschmalzhaf-cell/Gideon-s-Tamagotschi?style=for-the-badge&color=E0316E&label=Release)](https://github.com/gschmalzhaf-cell/Gideon-s-Tamagotschi/releases)
[![CI](https://img.shields.io/github/actions/workflow/status/gschmalzhaf-cell/Gideon-s-Tamagotschi/ci.yml?style=for-the-badge&label=CI%20Build)](https://github.com/gschmalzhaf-cell/Gideon-s-Tamagotschi/actions)
[![Android](https://img.shields.io/badge/Android-8.0%2B%20(API%2026)-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)

<br/>

```text
╭──────────────────────────────────────────╮
│  ●  GIDEON-OS              👑 Erwachsen  │
│  ┌────────────────────────────────────┐  │
│  │        ✨ Mir geht's super!        │  │
│  │              👑                    │  │
│  │           ╭──────╮                 │  │
│  │          (  ^  ^  )  💖            │  │
│  │           ╰──︶──╯                 │  │
│  │  ~~~~~~~✿~~~~~~~~~~~~~✿~~~~~~~~~~  │  │
│  └────────────────────────────────────┘  │
│   [ 🍔 Füttern ] [ 🎾 Spielen ] [ 🛁 ]   │
╰──────────────────────────────────────────╯
```

[⬇️ Neueste APK herunterladen](https://github.com/gschmalzhaf-cell/Gideon-s-Tamagotschi/releases/latest) · [✨ Features](#-features) · [🎨 Grafik & UI](#-grafik--ui-highlights) · [🚀 Ideen für die Zukunft](#-ideen-für-die-zukunft-roadmap--backlog) · [🛠 Für Entwickler](#-für-entwickler)

</div>

---

## 📖 Die Legende von Gideon

> **Gideon's Tamagot(s)chi** – ja, mit Klammer-s. Weil Gideon manchmal eins braucht und manchmal keins.

Es war einmal ein Entwickler, der eines Morgens in seiner Hosentasche ein leises *Piep* hörte. Dort saß Gideon – ein kleiner, pinker Pixelhaufen mit zwei Augen. *„Fütter mich“* sagte er nicht, aber seine Augen sagten es. Seitdem wohnt Gideon auf seinem Android-Telefon. Er isst nicht viel (nur alle paar Minuten ein bisschen Aufmerksamkeit), aber wenn du ihn vergisst, merkt er sich das. Den Rest vergisst er sofort wieder. Ein wahres Tamagot(s)chi eben.

Füttere dein Pet, spiel mit ihm, leg es schlafen, bade es und pflege es gesund. Vernachlässigst du es, wird es erst bockig, dann krank – und bei Gesundheit **0** heißt es **Game Over**. Bei liebevoller Pflege wächst es dagegen vom winzigen **Baby** über **Kind** und **Teenager** bis zum gekrönten **Erwachsenen** heran!

---

## ✨ Features

| Bereich | Was dich erwartet |
| :--- | :--- |
| 🥚 **Pet-Brutstation** | Wähle **Name** (inkl. 🎲 Zufalls-Namensgenerator), **Spezies** (*Blob*, *Keks*, *Stern*) und **8 leuchtende Farben** mit animierter Live-Vorschau. |
| 📟 **Retro-Handheld-Habitat** | Ein liebevoll gestaltetes Tamagotchi-Gehäuse mit pulsierender Status-LED, animiertem Tag-/Nacht-/Krank-Himmel, wandernden Wolken, Sternen, Wiesenblumen und subtiler LCD-Scanline-Textur. |
| 🎭 **Lebendige Emotionen** | Blinzelnde Augen, Atem-Squash-&-Stretch, dynamische Mimik für *Glücklich*, *Entspannt*, *Hungrig*, *Schmutzig*, *Müde*, *Krank*, *Schlafend* und *Geist* – plus Streichel-Reaktion beim Antippen! |
| 👑 **Evolution & Accessoires** | **Baby** (Locke 🐣) → **Kind** (Spross 🌱) → **Teenager** (Schleife 🎀) → **Erwachsener** (schwebende Goldkrone & Aura 👑). |
| 🎮 **5 Pflege-Aktionen** | 🍔 **Füttern**, 🎾 **Spielen**, 🛁 **Waschen**, 😴 **Schlafen / ☀️ Aufwecken**, 💊 **Medizin** – jeweils mit eigenen Partikel-Effekten im Habitat und Sound-Feedback. |
| 📊 **Übersichtliches Dashboard** | Animierte Gesundheits-Hauptleiste (mit Puls-Warnung bei < 30 %) + 2×2-Kartenraster für Hunger, Glück, Energie & Sauberkeit. |
| 🌙 **Lebt im Hintergrund weiter** | Vordergrund-Tick (alle 60 s) & Hintergrund-Tick via **WorkManager** (alle 15 min) inklusive *„Während du weg warst …“*-Rückblick-Dialog. |
| 🔔 **Benachrichtigungen & Settings** | Meldet sich rechtzeitig bei Hunger, schlechter Gesundheit oder Heimweh – alles sauber steuerbar über **DataStore**-Einstellungen. |
| 🕊️ **Atmosphärischer Game Over** | Sternenklarer Abschieds-Screen mit schwebendem Geister-Pet, Heiligenschein, erreichter Entwicklungsstufe und Neustart-Button. |

---

## 🎨 Grafik & UI-Highlights

- **Eigenständiges Retro-Pastell-Designsystem**: Warmes Berry-Pink & Pastell-Töne im **Light Mode**, gemütliches Midnight-Arcade-Plum im **Dark Mode**.
- **100 % Custom Jetpack Compose Canvas Art**: Jede Spezies (*Blob*, *Keks* mit Schokostückchen, *Plüsch-Stern*) wird vektorbasiert mit Glanzlichtern, Schattierung, weicher Kontur und stufenabhängigen Accessoires gerendert.
- **Taktile 3D-Arcade-Buttons**: Griffige Aktionsknöpfe mit Feder-Druckanimation und 3D-Kante wie bei echten Taschen-Konsolen.
- **Neues adaptives App-Icon**: Glossy Gideon-Blob mit kleiner Krone in einer Tamagotchi-Ei-Silhouette.

---

## 🚀 Ideen für die Zukunft (Roadmap & Backlog)

Du willst wissen, wohin sich **Gideon's Tamagot(s)chi** noch entwickeln kann? Hier ist unsere prall gefüllte Ideensammlung für kommende Versionen:

### 🕹️ Gameplay & Minispiele
- [ ] **Minispiel „Keks-Regen“**: Fange herunterfallende Leckereien und weiche verdorbenem Gemüse aus, um Bonus-Glück & Münzen zu verdienen.
- [ ] **Minispiel „Simon Says / Gedächtnis-Piep“**: Merke dir bunte Retro-Signale auf der Konsole und wiederhole sie für Extra-XP.
- [ ] **Minispiel „Stein-Schere-Blatt“**: Spiele schnelle Runden direkt gegen dein Pet mit witzigen Reaktionen.
- [ ] **Schwierigkeitsgrade**: *Entspannt* (langsamerer Verfall für den Alltag), *Klassisch* (aktuell) und *Hardcore-Retro* (gnadenloses 90er-Tempo).
- [ ] **Geheime Evolutionen**: Abhängig vom Pflegestil (z. B. viel gespielt vs. viel gefüttert) entwickelt sich das Pet in seltene Spezialformen (*Sport-Blob*, *Schlemmer-Keks*, *Kosmos-Stern*).

### 🛍️ Shop, Inventar & Personalisierung
- [ ] **Münzsystem & Snack-Shop**: Verdiene Pixel-Münzen durch Pflege-Streaks & Minispiele und kaufe verschiedene Gerichte (*Sushi*, *Pizza*, *Erdbeertorte*, *Brokkoli*), die unterschiedliche Effekte haben.
- [ ] **Kleiderschrank & Accessoires**: Freischaltbare Hüte, Brillen, Schals, Zauberstäbe oder Kopfhörer zum freien Kombinieren.
- [ ] **Gehäuse-Themes (Shell Skins)**: Wechsle das Aussehen deiner Tamagotchi-Konsole – z. B. *90er Transparent-Lila*, *GameBoy-Grün*, *Cyberpunk-Neon*, *Matcha-Pastell* oder *Gold-Edition*.
- [ ] **Eigene Raum-Hintergründe**: Dekoriere das Habitat mit Möbeln, Pflanzen, Lichterketten, Strand- oder Weltraum-Kulissen.

### 🌍 Lebendige Welt & Ereignisse
- [ ] **Echtzeit-Tag/Nacht-Zyklus & Wetter**: Das Habitat spiegelt echte Tageszeit (Sonnenaufgang, Abendrot, Sternenhimmel) und zufälliges Wetter (Sonne, Regen, Schneeflocken, Regenbogen) wider.
- [ ] **Saisonale Events**: Spezielle Deko & Snacks zu Halloween, Weihnachten, Ostern oder dem eigenen Geburtstag des Pets.
- [ ] **Tagebuch & Meilensteine**: Eine chronologische Lebensgeschichte deines Pets (*„Tag 1: Geschlüpft“*, *„Tag 3: Wurde zum Teenager“*, *„100× gefüttert“*) inklusive **Hall of Fame** für frühere Pets.
- [ ] **Erfolge & Badges**: Sammle Abzeichen wie *„Meisterkoch“*, *„Saubermann“*, *„7 Tage überlebt“* oder *„Nachteule“*.

### 📱 Android-Integration & Komfort
- [ ] **Home-Screen-Widget (Glance)**: Sieh dein Pet, seine Stimmung und die wichtigsten Balken direkt auf dem Android-Homescreen – inklusive Schnell-Füttern!
- [ ] **Haptisches Feedback**: Sanfte Vibrationen beim Streicheln, Füttern, Level-Up oder kritischen Warnungen.
- [ ] **Chiptune-Soundboard**: Echte kleine 8-Bit-Retro-Synthesizer-Klänge (Essen, Blubbern, Einschlafen, Evolution-Fanfare) statt nur System-Tönen.
- [ ] **Pausen- / Urlaubsmodus („Babysitter“)**: Schicke dein Pet vorübergehend in die „Pixel-Kita“, wenn du im Urlaub oder in wichtigen Terminen bist.

### 🤝 Soziales & Technik
- [ ] **Mehrere Pets gleichzeitig**: Halte eine kleine WG aus mehreren Pets und wechsle per Swipe zwischen ihnen.
- [ ] **Lokales Treffen (QR-Code / Nearby)**: Tausche kleine Geschenke mit Freunden oder lass eure Pets für einen Moment gemeinsam im Habitat spielen.
- [ ] **Export / Backup**: Sichere deinen Spielstand lokal als JSON-Backup oder über Android Auto Backup.

---

## 📦 Download & Installation

Fertige, signierte Releases (**APK** & **AAB**) findest du unter [**GitHub Releases**](https://github.com/gschmalzhaf-cell/Gideon-s-Tamagotschi/releases):

1. Lade die aktuelle `app-release.apk` herunter.
2. Öffne die Datei auf deinem Android-Gerät (Android 8.0+ / API 26+) und erlaube ggf. die Installation aus dieser Quelle.
3. App starten, Pet ausbrüten – und **füttern nicht vergessen!** 😉

---

## 🛠 Tech-Stack & Architektur

- **UI & Grafik**: Jetpack Compose, Material 3, Custom Compose `Canvas` Artwork & Animationen
- **Architektur**: MVVM + Clean-inspirierte Trennung (`ui`, `domain`, `data`, `di`)
- **Dependency Injection**: Hilt + KSP
- **Persistenz**: Room Database (Pet-Zustand) + DataStore Preferences (Einstellungen)
- **Hintergrund-Logik**: WorkManager (`PetTickWorker`, `PetNotificationWorker`)
- **Navigation**: Navigation Compose
- **CI / CD**: GitHub Actions (`ci.yml` für Build & Unit-Tests auf PRs, `release.yml` für signierte APK/AAB Releases auf Tag `v*`)

---

## 💻 Für Entwickler

**Voraussetzungen:** JDK 17 und Android SDK (`compileSdk 35`).

Das Gradle-Wrapper-Jar ist nicht eingecheckt und wird einmalig generiert mit:

```bash
gradle wrapper --gradle-version 8.10.2
```

Danach bauen & testen:

```bash
./gradlew assembleDebug       # Debug-APK erstellen
./gradlew testDebugUnitTest   # Unit-Tests (TickCalculator, EvolutionEngine) ausführen
```

### Entwicklungs-Historie

1. **Phase 1**: Projektgerüst (Gradle, Hilt, Navigation, Theme)
2. **Phase 2**: Pet erstellen + Home-Screen mit Statusbalken + Room
3. **Phase 3**: Aktionen + Foreground-Game-Tick
4. **Phase 4**: WorkManager-Background-Ticks, Offline-Sync, Lebenszyklus/Evolution, GameOver
5. **Phase 5**: Benachrichtigungen, DataStore-Einstellungen, Sounds, Animationen
6. **Phase 6**: Unit-Tests + Release-Config (Signing, ProGuard, Release-Workflow)
7. **v1.1.0 (aktuell)**: Großes Grafik- & UI-Update (Retro-Handheld-Habitat, Custom Canvas Pet-Art für alle Spezies/Stufen/Stimmungen, Partikeleffekte, taktile Arcade-Buttons, neues App-Icon & erweiterte Roadmap)

---

<div align="center">
<sub>Gemacht mit 💖, Pixeln und einem optionalen (s) für Gideon.</sub>
</div>
