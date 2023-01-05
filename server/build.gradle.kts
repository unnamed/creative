plugins {
    id("creative.publishing-conventions")
}

dependencies {
    api(project(":creative-api"))
    testCompileOnly(project(":creative-serializer-minecraft"))
}