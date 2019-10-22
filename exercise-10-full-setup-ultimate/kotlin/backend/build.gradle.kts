import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.awt.Desktop
import java.net.URI

// Generated using: https://start.spring.io/#!language=kotlin&type=gradle-project

plugins {
    id("org.springframework.boot") version "2.1.8.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.2.71"
    kotlin("plugin.spring") version "1.2.71"
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

tasks {
    named<Copy>("processResources") {
        from(project(":frontend").buildDir) {
            into("static")
        }
        dependsOn(":frontend:build")
    }

    val bootJarArtifactName = "${rootProject.name}-${project.version}.jar"

    withType<BootJar> {
        archiveFileName.set(bootJarArtifactName)
    }

    register<Exec>("run") {
        commandLine = listOf("java", "-jar", "$buildDir/libs/$bootJarArtifactName")
        doFirst {
            Thread {
                Thread.sleep(5000)
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(URI("http://localhost:8080"))
                }
            }.start()
        }
        dependsOn("bootJar")
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
}
