## Installation

You can add creative to your project using [Gradle](https://gradle.org/)
*(recommended)*, [Maven](https://maven.apache.org/) or manually downloading the
JAR files


### Gradle

Add our repository

```kotlin
repositories {
    maven("https://repo.unnamed.team/repository/unnamed-public/")
}
```

Add the necessary dependencies

```kotlin
dependencies {
    // Core API, necessary for everything
    implementation("team.unnamed:creative-api:0.1.12-SNAPSHOT")
    
    // Resource Pack Server
    implementation("team.unnamed:creative-server:0.1.12-SNAPSHOT")
}
```

### Maven

Add our repository

```xml
<repository>
    <id>unnamed-public</id>
    <url>https://repo.unnamed.team/repository/unnamed-public/</url>
</repository>
```

Add the necessary dependencies

```xml
<dependency>
    <groupId>team.unnamed</groupId>
    <artifactId>creative</artifactId>
    <version>0.1.12-SNAPSHOT</version>
</dependency>
```