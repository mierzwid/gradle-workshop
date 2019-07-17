// A “lib” dependency is a special form of an execution dependency. It causes the other project to be built first
// and adds the jar with the classes of the other project to the classpath. It also adds the dependencies of the other
// project to the classpath. So you can enter the “api” directory and trigger a “gradle compile”. First the “shared”
// project is built and then the “api” project is built. Project dependencies enable partial multi-project builds.

// 1. setup configuration for all subprojects
// this is Java multi project
// personService depends on both api & shared
// api depends on shared
// all projects require junit:junit:4.12 for testing

subprojects {
    apply(plugin = "java")
    group = "org.gradle.sample"
    version = "1.0"
    repositories {
        mavenCentral()
    }
    dependencies {
        "testImplementation"("junit:junit:4.12")
    }
}

project(":api") {
    dependencies {
        "implementation"(project(":shared"))
    }
}

project(":services:personService") {
    dependencies {
        "implementation"(project(":shared"))
        "implementation"(project(":api"))
    }
}


