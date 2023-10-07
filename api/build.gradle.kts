plugins {
    id("creative.publishing-conventions")
}

dependencies {
    compileOnlyApi("org.jetbrains:annotations:23.0.0")
    api("net.kyori:adventure-key:4.9.3")
    api("net.kyori:adventure-api:4.14.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.14.0")
}