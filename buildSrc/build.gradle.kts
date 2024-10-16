plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("gradle.plugin.org.cadixdev.gradle:licenser:0.6.1")
}

tasks {
    compileJava {
        options.release.set(8)
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}