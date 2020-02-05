# Exercise 7: Multi-project builds

Based on [Authoring Multi-Project Builds](https://docs.gradle.org/current/userguide/multi_project_builds.html)

The powerful support for multi-project builds is one of Gradle’s unique selling points. A multi-project build in gradle consists of one root project, and one or more subprojects that may also have subprojects.
                                                                                        
Let's start with simple structure: root project "water" and one subproject "bluewhale"

`mkdir bluewhale`

When we need to configure it in `settings.gradle.kts`: 

```kotlin
rootProject.name = "water"

include("bluewhale")
```

As you probably remember, this configuration in initial phase, so Gradle knows basic project structure.

## Configure all 

So far, we were configuring only single project builds. Now, new options become vital. Let's configure all projects at once!

```kotlin
allprojects {
    // when we are in a subproject we need to register the hello task since it is not yet present there
    tasks.register("hello") {
        doLast {
            println("I'm ${project.name}")
        }
    }
}
```

## Configure current

```kotlin
tasks.named("hello") { doFirst { println("Welcome to The Water project") } }
```

## Configure subprojects

```kotlin
subprojects {
    tasks.named("hello") {
        doLast {
            println("- I depend on water")
        }
    }
}
```

## Configure specific

```kotlin
// 4. Configuring specific projects
project(":bluewhale") {
    tasks.named("hello") {
        doLast {
            println("- I'm the largest animal that has ever lived on this planet.")
        }
    }
}
```

## Specific behaviour placed in projects script
 
Let's create project krill:
`mkdir krill`
`touch krill/build.gradle.kts`

```kotlin
tasks.named("hello") {
    doLast {
        println("- The weight of my species in summer is twice as heavy as all human beings.")
    }
}
```

## Dynamic configuration

Let's decide at runtime about which project to configure:

```kotlin
configure(subprojects.filter { it.name != "tropicalFish" }) {
    tasks.named("hello") {
        doLast {
            println("- I love to spend time in the arctic waters.")
        }
    }
}
```

### Filtering via properties

Add tropicalFish project:
`mkdir tropicalFish`

Decide which project is a fish:
```kotlin
extra["isFish"] = false
```

```kotlin
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
```

## Tasks filtering for execution

Let's switch to the bluewhale project dir and execute from there. What is your guess?

`cd bluewhale`
`../gradlew -q hello`

Gradle always evaluates all projects of a multi-project build and only then filters tasks by parameters and current dir.

## Specific tasks

Add task only to specific projects and execute it from toplevel
Go to bluewhale and krill projects:

```kotlin
tasks.register("distanceToIceberg") {
    doFirst {
        println("X nautical miles")
    }
}
```

Run: `/gradlew distanceToIceberg`

## Execute tasks by absolute paths

`cd tropicalFish ; ../gradlew -q :hello :krill:hello hello`

## Apply plugin outside the `plugins` section

```kotlin
subprojects {
    apply(plugin = "java")
    
    dependencies {
        "testImplementation"("junit:junit:4.12")
    }
}
```