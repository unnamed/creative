plugins {
    `java-library`
}

repositories {
    mavenLocal()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/central/")
    maven("https://repo.codemc.io/repository/nms/")
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:21.0.0")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    processResources {
        filesMatching("**/*.yml") {
            filter<org.apache.tools.ant.filters.ReplaceTokens>(
                "tokens" to mapOf("version" to project.version)
            )
        }
    }
}