import com.moowork.gradle.node.npm.NpmTask

plugins {
    id("com.github.node-gradle.node") version "2.0.0"
}

node {
    version = "10.16.1"
    download = true
}

tasks {
    register<NpmTask>("buildReact") {
        setArgs(listOf("run", "build"))

        inputs.file("package.json")
        inputs.dir("public")
        inputs.dir("src")
        outputs.dir(buildDir)

        dependsOn("npmInstall")
    }

    named("build") {
        dependsOn("buildReact")
    }
}