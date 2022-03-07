plugins {
    id("creative.java-conventions")
    `maven-publish`
}

val snapshotRepository: String by project
val releaseRepository: String by project

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