# Exercise 4: Build lifecycle

Gradle build lifecycle is not an equivalent of the Maven build lifecycle. It is a specific trait of the Gradle build tool. If we want to see build phases like compile, test, package, etc., you need to refer to specific Gradle plugins, like Java plugin, which introduce those lifecycle tasks. 

However, this exercise is not about lifecycle tasks, but rather about how Gradle works internally which is important to know when developing build logic.

## Tracing the build

There are three main phases of the build:
1. Initialization
2. Configuration
3. Execution

### Initialization - a general configuration of the Gradle tool

Let's see which files are executed at the initialization phase and why.

Place `init.gradle.kts` in `~/.gradle` directory. This configuration will take effect for all Gradle processes run on your machine.
You can configure here company-wide repositories, defaults you want to use everywhere, etc.

```kotlin
//1. place this file in ~/.gradle directory
println("Gradle Workshop: init.gradle.kts : This is initialization phase.")

// Script object: https://docs.gradle.org/current/dsl/org.gradle.api.Script.html

logger.lifecycle("Gradle Workshop: init.gradle.kts : Logging in init phase!")

// Gradle object: https://docs.gradle.org/current/dsl/org.gradle.api.invocation.Gradle.html

logger.lifecycle("Gradle home: $gradleHomeDir")
logger.lifecycle("Gradle version: $gradleVersion")
logger.lifecycle("Gradle start params: $startParameter")
```

### Initialization - project specific

Before Gradle can configure the project, it needs to know basic information about the project structure. This is why it loads `settings.gradle.kts` from the project root directory. One project, no matter if it is multi-modular or simple one script can have only one `settings.gradle.kts` file.

```kotlin
rootProject.name = "gradle-workshop"

//1.
println("Gradle Workshop: settings.gradle.kts : This is also initialization phase!")

// Script Object: https://docs.gradle.org/current/dsl/org.gradle.api.Script.html
logger.lifecycle("Current dir: ${file(".").path}")

// we have access to project file tree at this point
fileTree(".") {
    files.filter { it.path.contains(".") }.forEach { file ->
        logger.lifecycle("- ${file.path}")
    }
}

// we can perform file operations
mkdir(".aem")
mkdir(".gap")

delete(".aem", ".gap")

// Settings Object: https://docs.gradle.org/current/dsl/org.gradle.api.initialization.Settings.html

logger.lifecycle("Root dir: $rootDir")
logger.lifecycle("Settings: $settingsDir")

// plus configuration of projects which we will cover in multiproject builds: include(projectPaths), etc.
//include("bluewhale", "krill", "tropicalFish")

// accessing properties: https://kotlinlang.org/docs/reference/delegated-properties.html

val property: String by settings
logger.lifecycle("Property value: $property")
```

As you can see, a lot is possible at that time, we can even create a dynamic project structure. In spite of everything, make is as lightwaight as possible, since it will affect every invocation of Gradle for this project, even `./gradlew help`.

### Configuration - :buildSrc

Before Gradle will jump into a project-specific configuration, it first executes configuration of the special `:buildSrc` project. This project is special because it can define all the required plugins and its versions that are needed for our project. It is possible to point out there all the required dependencies for our build (Gradle Plugins) - to distinguish from project-specific dependencies (Java libraries, project artifact dependencies, etc.).  

```kotlin
logger.lifecycle("Gradle Workshop: buildSrc/build.gradle.kts : Logging in :buildSrc configuration phase!")
```

We will learn about this later on.

### Configuration - :

Then we finally go to the flesh and bones of the Gradle build. When the root project `:` and all of the sub-project (if mentioned in `settings.gradle`) are configured.

```kotlin
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
``` 

What is important here, having project configuration is also run for every build. Try running `./gradlew hello` and then `./gradlew help` to see all the configuration is executed. So this is important to avoid heavy lifting at this point.

### Execution

Finally, we came to execution. When Gradle is done with initialization and whole configuration, it is ready to execute tasks specified. It now has full tasks graph ready, so it is easy to filter those ones which were requested from commandline, e.g. `./gradlew hello`.

Execution runs `doFirst` and `doLast` code blocks from tasks configuration - se in logs.

## Backing objects

Worth to mention, that each script has its backing object, that provides some API accessible during its execution. You saw it in the code, properties available like `logger`, `project`, etc. 

We can sum it up here (phases/scripts/backing objects): 

1. Initialization​
    * $HOME/.gradle/init.gradle.kts​
        * Gradle as the backing object​
    * $PROJECT_DIR/settings.gradle.kts​
        * Settings as the backing object​
2. Configuration​
    * $PROJECT_DIR/buildSrc/build.gradle.kts​
        * Gradle as the backing object​
    * $PROJECT_DIR/build.gradle.kts​
        * Project as the backing object​
3. Execution​
    * $PROJECT_DIR/build.gradle.kts​
        * Task as the backing object​
