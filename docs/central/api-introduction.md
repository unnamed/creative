## Developer API Introduction

### Adding central to your project
The first step to using `creative-central` is to add it to your project, you can do
this using Gradle *(recommended)* or Maven

#### Gradle
If you are using Gradle, you have to add our repository and the central dependency
to your build script *(`build.gradle(.kts)`)*

##### Kotlin DSL:
```kotlin
repositories {
    maven("https://repo.unnamed.team/repository/unnamed-public/")
}

dependencies {
    compileOnly("team.unnamed:creative-central-api:%%REPLACE_latestReleaseOrSnapshot{team.unnamed:creative-central-api}%%")
}
```

##### Groovy DSL:
```groovy
repositories {
    maven {
        url "https://repo.unnamed.team/repository/unnamed-public/"
    }
}

dependencies {
    compileOnly "team.unnamed:creative-central-api:%%REPLACE_latestReleaseOrSnapshot{team.unnamed:creative-central-api}%%"
}
```

#### Maven
If you are using Maven, you have to add our repository and the central dependency
to your `pom.xml`
```xml
<repositories>
    <repository>
        <id>unnamed-public</id>
        <url>https://repo.unnamed.team/repository/unnamed-public/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>team.unnamed</groupId>
        <artifactId>creative-central-api</artifactId>
        <version>%%REPLACE_latestReleaseOrSnapshot{team.unnamed:creative-central-api}%%</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Declaring a dependency on central

If you are developing a plugin that uses `creative-central`, you should declare a dependency
on it in your plugin's `plugin.yml`, this will make sure that your plugin will not be loaded
if `creative-central` is not present

**plugin.yml in Bukkit plugins**
```yaml
name: ...
main: ...
version: ...
...
depend: [creative-central]
```

### Getting an instance of the API

First of all, you need to obtain an instance of `CreativeCentral`, you can do this by using
your platform's service provider *(preferred)* or using `CreativeCentralProvider#get()`, the
static service provider

**Using Bukkit's service provider**
```java
RegisteredServiceProvider<CreativeCentral> provider = Bukkit.getServicesManager().getRegistration(CreativeCentral.class);
if (provider != null) {
    CreativeCentral central = provider.getProvider();
    // ...
}
```

**Using the static service provider**
```java
CreativeCentral central = CreativeCentralProvider.get();
```

Now that we have a `CreativeCentral` instance, we can get to the next step