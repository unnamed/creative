plugins {
    id("creative.publishing-conventions")
}

description = "A resource-pack library for Minecraft: Java Edition."

dependencies {
    compileOnlyApi("org.jetbrains:annotations:26.0.2")
    api("net.kyori:adventure-key:4.9.3")
    api("net.kyori:adventure-api:4.14.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.14.0")
}