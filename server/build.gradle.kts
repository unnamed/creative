plugins {
    id("creative.publishing-conventions")
}

dependencies {
    api(project(":creative-api"))
    testImplementation(project(":creative-serializer-minecraft"))
}