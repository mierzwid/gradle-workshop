// 1. Configuration vs execution: ./gradlew tasks

// create directory with files


// for other types of tasks to work with files please see: https://docs.gradle.org/current/userguide/working_with_files.html
tasks.register("myFirstTask") {
    logger.lifecycle("Configuring myFirstTask")
    doLast {
        logger.lifecycle("Running my first task: Hello Dolly!")
    }
}

//tasks.getByName("myFirstTask")

tasks {
    group = "Exercise 5 - tasks"
    val resourcesDestination = "$buildDir/resources"

    val copyMyFiles = register<Copy>("copyMyFiles") {
        from(fileTree("src").filter { it.name.contains(".json") }.files)
        into(resourcesDestination)
    }

    copyMyFiles {
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

// see more on developing custom tasks: https://docs.gradle.org/current/userguide/custom_tasks.html
    val reportDestination = "$buildDir/reports"
    val validationMode = project.properties["gradle-workshop.validation.mode"]
    register("verifyTheCopy") {
        inputs.dir(resourcesDestination)
        inputs.property("mode", validationMode)
        outputs.dir(reportDestination)
        doLast {
            fileTree(resourcesDestination).forEach {
                logger.lifecycle("Validation mode: $validationMode, file: ${it.name}")
            }
            File(reportDestination, "report.txt").writeText("OK")
        }
    }

    register("build") {
        dependsOn("copyMyFiles", "verifyTheCopy")
    }

// 3. Show parallel tasks execution by removing mustRunAfter
// ./gradlew meeting

    register("hello") {
        doFirst {
            Thread.sleep(1000)
            logger.lifecycle("Hello!")
        }
        finalizedBy("by")
    }

    register("by") {
        doFirst {
            logger.lifecycle("By!")
        }
//        mustRunAfter("hello")
    }

    register("meeting") {
        doLast {
            throw Exception("Failed!")
        }
        dependsOn("hello")
        finalizedBy("by")
    }
}


// Exercise: More insight into your build
// ./gradlew -Dorg.gradle.internal.tasks.stats hello
// ./gradlew --scan hello
