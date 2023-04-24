plugins {
    id("creative.java-conventions")
}

dependencies {
    implementation(project(":creative-central-api"))
    implementation(project(":creative-serializer-minecraft"))
    implementation(project(":creative-server"))
}