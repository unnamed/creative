rootProject.name = "creative-parent"

includePrefixed("api")
includePrefixed("server")

fun includePrefixed(name: String) {
    include("creative-$name")
    project(":creative-$name").projectDir = file(name)
}