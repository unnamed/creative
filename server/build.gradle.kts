plugins {
    id("creative.publishing-conventions")
}

description = "An standalone resource-pack server for the creative API"

dependencies {
    api(project(":creative-api"))
    testImplementation(project(":creative-serializer-minecraft"))
}