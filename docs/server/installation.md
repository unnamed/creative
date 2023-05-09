## Installation

You can add `creative-server` to your project using [Gradle](https://gradle.org/)
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
    implementation("team.unnamed:creative-server:%%REPLACE_latestReleaseOrSnapshot{team.unnamed:creative-server}%%")
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
    <artifactId>creative-server</artifactId>
    <version>%%REPLACE_latestReleaseOrSnapshot{team.unnamed:creative-server}%%</version>
</dependency>
```