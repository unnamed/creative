plugins {
    `java-library`
    id("org.cadixdev.licenser")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.0")
}

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

license {
    header.set(rootProject.resources.text.fromFile("header.txt"))
    include("**/*.java")
    newLine.set(false)
}

tasks {
    javadoc {
        isFailOnError = false
        (options as StandardJavadocDocletOptions).run {
            tags("sinceMinecraft:a:Since Minecraft:")
            tags("sincePackFormat:a:Since Resource-Pack Format:")
        }
    }
    test {
        useJUnitPlatform()
    }
}