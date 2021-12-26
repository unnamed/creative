plugins {
    `java-library`
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnlyApi("org.jetbrains:annotations:23.0.0")
    api(project(":api"))
    api(project(":serializer-default"))
}