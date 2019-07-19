//1. place this file in ~/.gradle directory
println("Gradle Workshop: init.gradle.kts : This is initialization phase.")

// Script object: https://docs.gradle.org/current/dsl/org.gradle.api.Script.html

logger.lifecycle("Gradle Workshop: init.gradle.kts : Logging in init phase!")

// Gradle object: https://docs.gradle.org/current/dsl/org.gradle.api.invocation.Gradle.html

logger.lifecycle("Gradle home: $gradleHomeDir")
logger.lifecycle("Gradle version: $gradleVersion")
logger.lifecycle("Gradle start params: $startParameter")

