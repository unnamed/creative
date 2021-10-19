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

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}