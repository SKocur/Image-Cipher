import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.0"
    id("org.openjfx.javafxplugin") version "0.1.0"
    application
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

group = "com.imagecipher"
version = "1.0.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.skocur.imagecipher.MainKt")
    applicationDefaultJvmArgs = listOf(
        "--add-modules", "javafx.controls,javafx.fxml",
        "--add-exports", "javafx.graphics/com.sun.javafx.application=ALL-UNNAMED"
    )
}

tasks {
    jar {
        manifest {
            attributes["Start-Class"] = "com.skocur.imagecipher.MainKt"
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.0")
    implementation("org.jetbrains:annotations:24.0.0")

    implementation("org.jcommander:jcommander:2.0")

    implementation("org.openjfx:javafx-controls:21")
    implementation("org.openjfx:javafx-fxml:21")
    implementation("org.openjfx:javafx-swing:21")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")

    implementation("org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0")
    implementation("com.jfoenix:jfoenix:9.0.10")

    implementation("io.reactivex.rxjava3:rxjava:3.1.6")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")

    implementation("com.jcabi:jcabi-manifests:1.1")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.7.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
