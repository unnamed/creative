## Installation

You can add `creative` to your project using [Gradle](https://gradle.org/)
*(recommended)*, [Maven](https://maven.apache.org/) or manually downloading the
JAR files from [GitHub Releases](https://github.com/unnamed/creative/releases).

Note that `creative` and all of its submodules are available in the
Maven Central Repository.

### Gradle

```kotlin
dependencies {
    implementation("team.unnamed:creative-api:%%REPLACE_latestRelease{team.unnamed:creative-api}%%")

    // Serializer for Minecraft format (ZIP / Folder)
    implementation("team.unnamed:creative-serializer-minecraft:%%REPLACE_latestRelease{team.unnamed:creative-serializer-minecraft}%%")

    // Resource Pack server
    implementation("team.unnamed:creative-server:%%REPLACE_latestRelease{team.unnamed:creative-server}%%")
}
```

### Maven

<!--@formatter:off-->
```xml
<dependency>
    <groupId>team.unnamed</groupId>
    <artifactId>creative-api</artifactId>
    <version>%%REPLACE_latestRelease{team.unnamed:creative-api}%%</version>
</dependency>
```
<!--@formatter:on-->