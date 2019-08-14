/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds/
 */

// 1. Configuration vs execution: ./gradlew tasks

val hello: TaskProvider<Task> = tasks.register("hello") {
    logger.lifecycle("Lite task configuration!")
    // Thread.sleep(5000) // this would slow down configuration phase! don't do heavy lifting outside task actions! (doFirst, doLast)
    doFirst {
        logger.lifecycle("Heavy lifting!")
        repeat(5) {
            Thread.sleep(1000)
            logger.lifecycle("$it")
        }
    }
    doLast {
        logger.lifecycle("Finalizing my work!")
    }
}

//hello.get() // defeats the concept of configuration avoidance when task it not run

logger.lifecycle("Lite project configuration!")

// 2. Tasks dependencies

tasks.register("by") {
    doLast {
        logger.lifecycle("Good by!")
    }
    mustRunAfter(hello)
}

tasks.register("helloAndBy") {
    dependsOn("hello", "by")
//    finalizedBy("by")
}

// 3. Show parallel tasks execution by removing mustRunAfter
// ./gradlew helloAndBy

// Exercise: check the difference between mustRunAfter and finalizedBy when hello task fails

// Docs for lazy configuration: https://docs.gradle.org/current/userguide/lazy_configuration.html
// ./gradlew -Dorg.gradle.internal.tasks.stats hello
// ./gradlew --scan hello

