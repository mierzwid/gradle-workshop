# Exercise 5: Tasks, ordering & caching

Let's have a look at tasks execution in Gradle. Since Gradle is a "tasks runner", the concept of a task is in the very heart of it. See more on task API: https://docs.gradle.org/current/dsl/org.gradle.api.Task.html

Please run: `./gradlew tasks`

Task named "tasks" allows you to see tasks defined for the project.  

## Defining tasks

Usually, we simply make use of tasks defined by plugins, so we don't need to define them by hand. We will have a look on it in the [Exercise 6 - Plugins](../../exercise-06-plugins-1-java/kotlin/README.md). However, when you need to do some custom logic, you'll deal with custom tasks.


### Lazy configuration
There are multiple ways to define tasks. Tasks are defined in project build files. In Kotlin DSL there is `tasks` object we can use. Let's see different ways to define a task:

```kotlin
tasks.register("myFirstTask") {
    logger.lifecycle("Configuring myFirstTask")
    doLast {
        logger.lifecycle("Running my first task: Hello Dolly!")
    }
}
```
We invoke here `register` function on `tasks` object. It is very desirable to use this function when registering new tasks. It promotes [lazy configuration](https://docs.gradle.org/current/userguide/lazy_configuration.html) concept. What does it mean? The configuration block of a task is run only when the task is expected to be run in the particular build:

```kotlin
{
    logger.lifecycle("Configuring myFirstTask")
    doLast {
        logger.lifecycle("Running my first task: Hello Dolly!")
    }
}
``` 

So when we invoke `./gradlew tasks` we won't see logger output from the configuration code, since "tasks" task doesn't depend on "myFirstTask". However, when we invoke `./gradlew myFirstTask` we will see both: configuration and execution output. Following this advice, we can speed up build execution and execute only this logic which is required.

If we would use `create` or `getByName` functions we would then request from Gradle fully constructed "myFirstTask" on every build, defeating the lazy configuration concept. Let's see:

```kotlin
tasks.getByName("myFirstTask")
```  
and execute: `./gradlew tasks`.

### Tasks types
In the previous example, we used the task of the generic `Task` type. However, even "unplugged" Gradle comes with a list of useful tasks types, e.g.:
* Checkstyle​
* Copy​
* Delete​
* Exec​
* Sign​
* Sync​
* Tar​
* Upload​
* WriteProperties​
* Zip​

It is not in the scope of this course to go over all of them but we will show some examples. You can refer to [Gradle docs](https://docs.gradle.org/current/dsl/org.gradle.api.Task.html) to see the full list of tasks types and their API.

Now, let's use `tasks` object in a more readable way, passing it a configuration lambda:

```kotlin
tasks {
    group = "Exercise 5 - tasks"
    val resourcesDestination = "$buildDir/resources"

    register<Copy>("copyMyFiles") {
        from(fileTree("src").filter { it.name.contains(".json") }.files)
        into(resourcesDestination)
    }
}
``` 

Notice, we specified a group for our tasks. We can organize them in a project using this concept. Now, within the tasks configuration block, our `this` is tasks object itself, we can omit `tasks.` prefixes.  

The first task type we'll exercise is `Copy`. Copy task allows us to easily copy files around - that's obvious. "copyMyFile" task is purely declarative. We define files we want to copy using `from` function and `into` specifies the destination. Let's add some files:

```shell script
mkdir src
touch src/file.json
touch src/file.xml
```

And run our task:

`./gradlew copyMyFiles`

We can see that `file.json` got copied to the `build` directory. Nice!

If we want to decorate tasks execution we can use `doFirst` & `doLast` functions:

```kotlin
named("copyMyFiles") {
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
```

We referenced task by name but we could also assign it to a variable and use it for configuration purposes. I hope you can see that the naming of those functions (`doFirst`, `doLast`) makes more sense if we decorate existing tasks.

## Dependencies

It is very common, that tasks depend on each other.  Gradle makes this easy, please see: 

```kotlin
register("hello") {
    doFirst {
        Thread.sleep(1000)
        logger.lifecycle("Hello!")
    }
}

register("by") {
    doFirst {
        logger.lifecycle("By!")
    }
    mustRunAfter("hello")
}

register("meeting") {
    dependsOn("hello", "by")
}
```

This code adds new tasks in the tasks section: "hello", "by" and "meeting". "meeting" depends on "hello" and "by". Let's execute it:

`./gradlew meeting`

```shell script
> Task :hello
Hello!

> Task :by
By!
```

Looks good. Notice, that in the "by" task definition we added `mustRunAfter("hello")`. It is not required but it is the only way to specify running order. Because Gradle emphasizes build parallelization, tasks might be run in parallel when possible. So simply specifying `dependsOn("hello", "by")` does not specify execution order. That's why we need to use `mustRunAfter`. Let's comment it out:

```kotlin
register("by") {
    doFirst {
        logger.lifecycle("By!")
    }
    // mustRunAfter("hello")
}
```

and run: `./gradlew meeting`.

```shell script
> Task :by
By!

> Task :hello
Hello!
```

As we can see, the execution order is not deterministic anymore.

### Cleanups

If our task performs some "dirty" logic, we can specify another task that would do the cleanup (no matter if our task was successful or failed). We can use `finalizedBy` to configure this:

```kotlin
register("meeting") {
    doLast {
        throw Exception("Failed!")
    }
    dependsOn("hello")
    finalizedBy("by")
}
```

`./gradlew meeting` will produce:

```shell script
> Task :hello
Hello!

> Task :meeting FAILED

> Task :by
By!

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':meeting'.
> java.lang.Exception: Failed!
```

### Debugging order

Simply: `./gradlew meeting --dry-run`

```shell script
:hello SKIPPED
:meeting SKIPPED
:by SKIPPED
```

Using this option, you can see which tasks are going to be executed without waiting for the whole build.

## Caching

Let's get back to our copy tasks and run `./gradlew build`. Now we can observe an interesting thing. Only "verifyTheCopy" task is executed. "copyMyFiles" has been cached - nice. To prevent this we can delete build dir:

`rm -rf build ; ./gradlew build` 

Caching is out of the box configured for tasks of type `Copy`. It can vastly improve performance and is the core feature of Gradle. Moreover, we can configure a global build cache for our company in the enterprise version of Gradle. This way, if somebody performed a build under the same conditions we will only use artifacts produced by that build - without the need to torture our machine with calculations, etc.

So how we can make use of cache in our custom tasks? Lets extend our "verifyTheCopy" task to make it cacheable:

```kotlin
val reportDestination = "$buildDir/reports"
val validationMode =project.properties["gradle-workshop.validation.mode"]
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
    dependsOn("copyMyFiles")
}
```

Two things: 
1. Task needs to have an output specified to be able to cache the result. Makes sense, what we would cache otherwise?
2. We can add inputs in the form of properties values or files and directories. Gradle checks them for changes and caches the result matching it with input conditions.
    
We will cover the more advanced scenario in one of the following exercises. More in the topic: https://docs.gradle.org/current/userguide/build_cache.html

## For volunteers: More insight into your build

Review following command output and try to improve your build:
* `./gradlew -Dorg.gradle.internal.tasks.stats meeting`
* `./gradlew --scan meeting`

## For volunteers: Implement "archiveMyCopy" task which will create zip of the copied files (remember to test caching)