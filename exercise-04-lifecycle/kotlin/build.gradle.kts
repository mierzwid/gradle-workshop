/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds/
 */

println("Gradle Workshop: build.gradle.kts : This is configuration phase!")

tasks.register("hello") {
    doFirst {
        println("Gradle Workshop: build.gradle.kts : This is execution phase!")
    }
}