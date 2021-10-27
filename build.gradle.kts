plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven("https://repo.codemc.io/repository/nms/")
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:21.0.0")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

val snapshotRepository: String by project
val releaseRepository: String by project

tasks {
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