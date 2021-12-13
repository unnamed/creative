plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnlyApi("org.jetbrains:annotations:23.0.0")
    api("net.kyori:adventure-key:4.9.3")
}

val snapshotRepository: String by project
val releaseRepository: String by project

tasks {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }

    test {
        useJUnitPlatform()
    }

    publishing {
        repositories {
            maven {
                url = if (project.version.toString().endsWith("-SNAPSHOT")) {
                    uri(snapshotRepository)
                } else {
                    uri(releaseRepository)
                }
                credentials {
                    username = project.properties["UNNAMED_REPO_USER"] as String?
                        ?: System.getenv("REPO_USER")
                    password = project.properties["UNNAMED_REPO_PASSWORD"] as String?
                        ?: System.getenv("REPO_PASSWORD")
                }
            }
        }
        publications {
            create<MavenPublication>("maven") {
                from(getComponents().getByName("java"))
            }
        }
    }
}