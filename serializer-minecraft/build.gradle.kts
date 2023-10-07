plugins {
    id("creative.publishing-conventions")
}

dependencies {
    api(project(":creative-api"))
    api("com.google.code.gson:gson:2.8.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.14.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.14.0")
}