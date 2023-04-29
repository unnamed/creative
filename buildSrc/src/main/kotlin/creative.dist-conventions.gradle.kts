plugins {
    id("creative.java-conventions")
    id("com.github.johnrengelman.shadow")
}

tasks {
    processResources {
        filesMatching("**.yml") {
            expand("project" to project)
        }
    }
}