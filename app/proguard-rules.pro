# Room-Entities und Domain-Modelle duerfen nicht wegoptimiert/umbenannt werden
-keep class com.gideon.tamagotchi.data.local.** { *; }
-keep class com.gideon.tamagotchi.domain.model.** { *; }

# Hilt, Room, Compose und WorkManager liefern eigene Consumer-Regeln in ihren AARs.
