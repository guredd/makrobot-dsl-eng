group = "learn.makrobot"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.4.10"
}

repositories {
    jcenter()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.0.3")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.3.1")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}