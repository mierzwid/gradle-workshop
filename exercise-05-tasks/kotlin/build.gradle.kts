/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds/
 */

// 1. Configuration vs execution: ./gradlew tasks

// create directory with files

val resourcesDestination = "$buildDir/resources"

// for other types of tasks to work with files please see: https://docs.gradle.org/current/userguide/working_with_files.html
tasks.register<Copy>("copyMyFile") {
    from("src")
    into(resourcesDestination)
}

val copy = tasks.named("copyMyFile") {
    doFirst {
        logger.lifecycle("Started: $name")
        repeat(5) {
            Thread.sleep(1000)
            logger.lifecycle("Copying: $it")
        }
    }
    doLast {
        logger.lifecycle("Done copying!")
    }
}

//copy.get() // defeats the concept of configuration avoidance when task it not run

// 2. Tasks dependencies

tasks.register("usingTheCopy") {
    doLast {
        fileTree(resourcesDestination).forEach {
            logger.lifecycle("COPIED FILE: ${it.path}")
        }
    }
}

tasks.register("build") {
    dependsOn("copyMyFile", "usingTheCopy")
}

// 3. Show parallel tasks execution by removing mustRunAfter
// ./gradlew meeting

tasks.register("hello") {
    doFirst {
        Thread.sleep(1000)
        logger.lifecycle("Hello!")
    }
//    finalizedBy("by")
}

tasks.register("by") {
    doFirst {
        logger.lifecycle("By!")
    }
    mustRunAfter("hello")
}

tasks.register("meeting") {
    dependsOn("hello", "by")
}

// Exercise: check the difference between mustRunAfter and finalizedBy when hello task fails

// Docs for lazy configuration: https://docs.gradle.org/current/userguide/lazy_configuration.html
// ./gradlew -Dorg.gradle.internal.tasks.stats hello
// ./gradlew --scan hello

