/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds/
 */

//2. this will go to the configuration phase of a project
println("Gradle Workshop: build.gradle.kts : This is configuration phase!")

tasks.register("hello") {
    doFirst {
        //3. this will get run during execution phase
        println("Gradle Workshop: build.gradle.kts : This is execution phase!")
        // it does not matter this prop is not defined at configuration phase - it will be needed only during execution of this particular task
        logger.lifecycle("${project.extra["myExtraPropertyUsedLaterOn"]}") // you must reference it via project since task has its own `extra`
    }
}

// Project Object: https://docs.gradle.org/current/dsl/org.gradle.api.Project.html

// Accessing gradle.properties
logger.lifecycle("Property value: ${property("property")}")

// Extra properties
extra["myExtraPropertyUsedLaterOn"] = "MY EXTRA PROPERTY"

// Project configuration

logger.lifecycle("Project name: $name")
logger.lifecycle("$state")
logger.lifecycle("Build dir: $buildDir")

// setting default tasks
defaultTasks("hello")

// Project can be extended by Plugins with additional properties:
// e.g. `applicationDefaultJvmArgs` added by the "application" plugin