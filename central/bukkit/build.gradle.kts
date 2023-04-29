plugins {
    id("creative.dist-conventions")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":creative-central-api"))
    implementation(project(":creative-serializer-minecraft"))
    implementation(project(":creative-central-common"))
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    shadowJar {
        dependencies {
            // all these dependencies are provided by the server
            exclude(dependency("com.google.code.gson:gson"))
            exclude(dependency("net.kyori:adventure-api"))
            exclude(dependency("net.kyori:adventure-key"))
            exclude(dependency("net.kyori:examination-api"))
            exclude(dependency("net.kyori:examination-string"))
        }
    }
}