## Installation

You can add `creative-api` to your project using [Gradle](https://gradle.org/)
*(recommended)*, [Maven](https://maven.apache.org/) or manually downloading the
JAR files


### Gradle
```kotlin
repositories {
    maven("https://repo.unnamed.team/repository/unnamed-public/")
}
```
```kotlin
dependencies {
    implementation("team.unnamed:creative-api:0.6.0-SNAPSHOT")
    
    // Serializer for Minecraft format (ZIP / Folder)
    implementation("team.unnamed:creative-serializer-minecraft:0.6.0-SNAPSHOT")
    
    // Resource Pack server
    implementation("team.unnamed:creative-server:0.6.0-SNAPSHOT")
}
```


### Maven

```xml
<repository>
    <id>unnamed-public</id>
    <url>https://repo.unnamed.team/repository/unnamed-public/</url>
</repository>
```
```xml
<dependency>
    <groupId>team.unnamed</groupId>
    <artifactId>creative-api</artifactId>
    <version>0.6.0-SNAPSHOT</version>
</dependency>
```