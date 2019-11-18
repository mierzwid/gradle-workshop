plugins {
    id("java")
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

sourceSets {
    create("intTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

//configurations["intTestImplementation"].extendsFrom(configurations.implementation.get())
val intTestImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

// configurations["intTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())
val intTestRuntimeOnly by configurations.getting {
    extendsFrom(configurations.runtimeOnly.get())
}

dependencies {
    implementation("org.apache.commons:commons-math3:3.6.1")
    intTestImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    intTestRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks {
    register<Test>("intTest") {
        description = "Runs integration tests."
        group = "verification"

        classpath = sourceSets["intTest"].runtimeClasspath
        testClassesDirs = sourceSets["intTest"].output.classesDirs

        useJUnitPlatform()
        testLogging.showStandardStreams = true

        shouldRunAfter("test")
    }

    named("check") { dependsOn("intTest") }
}

