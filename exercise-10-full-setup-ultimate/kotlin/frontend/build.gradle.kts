import com.moowork.gradle.node.npm.NpmTask

// Generated using: https://create-react-app.dev/docs/getting-started

plugins {
    id("com.moowork.node") version "1.3.1"
}

tasks {
    register<NpmTask>("buildReactApp") {
        dependsOn("npmInstall")
        setArgs(listOf("run", "build"))

        inputs.file("package.json")
        inputs.dir("src")
        inputs.dir("public")
        outputs.dir(buildDir)
    }
    named("build") {
        dependsOn("buildReactApp")
    }
}

node {
    version = "10.16.1"
    download = true
}