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

## Configuring all projects

So far, we were configuring only single project builds. Now, new options become vital.

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