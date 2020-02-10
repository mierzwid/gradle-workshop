// A “lib” dependency is a special form of an execution dependency. It causes the other project to be built first
// and adds the jar with the classes of the other project to the classpath. It also adds the dependencies of the other
// project to the classpath. So you can enter the “api” directory and trigger a “gradle compile”. First the “shared”
// project is built and then the “api” project is built. Project dependencies enable partial multi-project builds.

// 1. Setup configuration for all subprojects
// this is Java multi project
// personService depends on both api & shared
// api depends on shared
// all projects require junit:junit:4.12 for testing

// 2. If we are developing api project, we have 3 different options to build it. Try each of them.
// * build - builds, test, analyze current project (if there are plugins hooked), skips testing and analyzing of dependent projects
// * buildNeeded - builds, test, analyze current project together with all projects it depends on
// * buildDependents - builds, test, analyze current project, projects it depends on and all projects that depends on the current

subprojects {
    apply(plugin = "java-library")
    group = "org.gradle.sample"
    version = "1.0"
    repositories {
        jcenter()
    }
    dependencies {
        "testImplementation"("junit:junit:4.12")
    }
}

project(":api") {
    dependencies {
        "api"(project(":shared"))
    }
}

project(":services:personService") {
    dependencies {
        "implementation"(project(":api"))
    }
}
