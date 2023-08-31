plugins {
    `java-library`
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
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