plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "creative-parent"

includePrefixed("api")
includePrefixed("server")
includePrefixed("serializer-minecraft")

fun includePrefixed(name: String) {
    val kebabName = name.replace(':', '-')
    val path = name.replace(':', '/')

    include("creative-$kebabName")
    project(":creative-$kebabName").projectDir = file(path)
}