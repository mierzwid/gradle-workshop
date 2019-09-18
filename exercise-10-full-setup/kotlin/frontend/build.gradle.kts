import com.moowork.gradle.node.npm.NpmTask

plugins {
    id("base")
    id("com.moowork.node") version "1.3.1"
}

tasks {
    register<NpmTask>("buildReactApp") {
        dependsOn("npmInstall")
        setArgs(listOf("run", "build"))
    }
    register<Copy>("copyReactAppToAppClasspath") {
        dependsOn("buildReactApp")
        from(buildDir)
        into("${project(":backend").buildDir}/resources/main")
    }
}

node {
    version = "10.16.1"
    download = true
}