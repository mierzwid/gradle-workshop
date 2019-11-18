# Exercise 6: Plugins

Let's start with [Java plugin](https://docs.gradle.org/current/userguide/java_plugin.html) to see how it works. First, we apply the plugin in the plugins section, which is recommended way of doing it:

```kotlin
plugins {
    id("java")
}
```

We cannon execute any code in the plugins section, it is a static declaration of plugins. This restriction comes from the way it works. A build script is read twice - the first time, only the plugins section is evaluated to build proper classpath to compile the script. On the second run, the entire script is executed having required plugins loaded.

Let's see what is now available: `./gradlew tasks`. A list of new tasks is added to the build:

```shell script
Build tasks
-----------
assemble - Assembles the outputs of this project.
build - Assembles and tests this project.
buildDependents - Assembles and tests this project and all projects that depend on it.
buildNeeded - Assembles and tests this project and all projects it depends on.
classes - Assembles main classes.
clean - Deletes the build directory.
jar - Assembles a jar archive containing the main classes.
testClasses - Assembles test classes.

Verification tasks
------------------
check - Runs all checks.
test - Runs the unit tests.

Documentation tasks
-------------------
javadoc - Generates Javadoc API documentation for the main source code.
```

Lets run `./gradlew build --dry-run`

As you can see, the extended task graph follows the convention taken from the Maven build tool. The difference is, it is not represented as build phases (Maven) but rather as an acyclic, directed graph of tasks (the Gradle style).

NOTICE: The Java plugin attaches some of its tasks to the lifecycle tasks defined by the [Base Plugin](https://docs.gradle.org/current/userguide/base_plugin.html#sec:base_tasks) — which the Java Plugin applies automatically. This way we have, for example, the "clean" task available, that allows us to clean the build directory: `./gradlew clean`.

## Source sets

Java plugin adds two default source sets to follow the Maven convention:
1. main
2. test

Simplifying, source sets are configurations of input files sets for compilation. Our source for compilation. This is a project (configuration) convention used by Java plugin.

We will cover this part in the following example: [Java plugin - integration tests](../../exercise-06-plugins-2-int-test/kotlin/README.md).

## Dependencies

Then we need to get into the dependencies section, where we can define dependencies for our sources like other Java libraries.

```kotlin
dependencies {
    implementation("org.apache.commons:commons-math3:3.6.1")
}
```

In our case, we add "commons-math" library, since we use it in Java code. All those dependencies will be used in the compilation and running of our program. If we need to make distinctions and have different dependencies for compilation, and different used at runtime, Java plugin offers:
* compileOnly
* runtimeOnly
* known implementation
* and a few other, more specific configurations (referer to the [Java plugin documentation](https://docs.gradle.org/current/userguide/java_plugin.html#tab:configurations)).

## Repositories

Running build task will fail at this point. It is because we do not have a way to resolve our code dependencies. We need to configure that using another project configuration `repositories`:

```kotlin
repositories {
    mavenCentral()
}
```

Thanks to those lines, Gradle would know to use [Maven Central](https://search.maven.org/) portal to resolve all the Java dependencies.

Now run `./gradlew build` and review the `build/classes` directory.

## Extensions

Beyond standard configurations that can be used by a plugin (`repositories`, `dependencies`), a plugin can add its custom extensions. Java plugin does this by adding `java` extension:

```kotlin
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}
```

As you can see, we can customize here plugin behavior.
