// 1. Configuring all projects at once
allprojects {
    // when we are in a subproject we need to register the hello task since it is not yet present there
    tasks.register("hello") {
        doLast {
            println("I'm ${project.name}")
        }
    }
}

// 2. Configuring current project - based on build script location
tasks.named("hello") { doFirst { println("Welcome to The Water project") } }

// 3. Configuring sub-projects only
subprojects {
    tasks.named("hello") {
        doLast {
            println("- I depend on water")
        }
    }
}

// 4. Configuring specific projects
project(":bluewhale") {
    tasks.named("hello") {
        doLast {
            println("- I'm the largest animal that has ever lived on this planet.")
        }
    }
}

// 5. Specific behaviour placed in projects script - see project krill

// 6. Dynamic configuration via filtering

configure(subprojects.filter { it.name != "tropicalFish" }) {
    tasks.named("hello") {
        doLast {
            println("- I love to spend time in the arctic waters.")
        }
    }
}

// 7. Filtering via properties - configuration dependencies

subprojects {

    afterEvaluate {
        if (extra["isFish"] as Boolean) {
            val hello by tasks.existing
            hello {
                doLast {
                    println("- I'm a fish")
                }
            }
        }
    }
}

// 8. Let's switch to the bluewhale project dir and execute from there. What is your guess?

// ../gradlew -q hello
//Gradle always evaluates all projects of a multi-project build and only then filters tasks by parameters and current dir.

// 9. Add task only to specific projects and execute it from toplevel
// go to bluewhale and krill projects
// ./gradlew distanceToIceberg

// 10. Execute tasks by their absolute path

// cd tropicalFish ; ../gradlew -q :hello :krill:hello hello

// 11. Apply plugin outside the `plugins` section

subprojects {
    apply(plugin = "java")

    dependencies {
        "testImplementation"("junit:junit:4.12")
    }
}