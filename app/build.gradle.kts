// build.gradle.kts (app)

plugins {
    id("com.android.application")
    // ...
    id("kotlin-kapt") // Use KAPT se ainda não for Kotlin 2.0 (caso contrário, use KSP)
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" // Use KSP
}

dependencies {
    // Jetpack Compose
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")

    // ROOM (SQLite Local)
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version") // Processador de anotações
    implementation("androidx.room:room-ktx:$room_version") // Coroutines
}