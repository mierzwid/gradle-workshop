# Exercise 6: Java plugin - integration test

Based on: https://docs.gradle.org/current/userguide/java_testing.html#sec:configuring_java_integration_tests

To have a better understanding of plugins mechanics let's define a new "phase" in our project build called "integration tests". This is a common case when we need to have it separated from the default "test phase". We need to take four steps:
1. Create a new source set
2. Add the dependencies to the appropriate configurations for that source set
3. Configure the compilation and runtime classpaths for that source set
4. Create a task to run the integration tests

## Create a new source set

```kotlin
sourceSets {
    create("intTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}
```

This will set up a new source set called intTest that automatically creates: 
* intTestImplementation, intTestCompileOnly, intTestRuntimeOnly configurations,
* a compileIntTestJava task that will compile all the source files under src/intTest/java

## Add the dependencies you need to the appropriate configurations for that source set

```kotlin
//configurations["intTestImplementation"].extendsFrom(configurations.implementation.get())
val intTestImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

//configurations["intTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())
val intTestRuntimeOnly by configurations.getting {
    extendsFrom(configurations.runtimeOnly.get())
}
```

This adds all the declared dependencies of the production code to also become dependencies of the integration tests. Same with runtime configuration.

NOTICE: In most cases, you want your integration tests to have access to the classes under test, which is why we ensure that those are included in the compilation and runtime classpaths in this example. But some types of tests interact with the production code in a different way. For example, you may have tests that run your application as an executable and verify the output. In the case of web applications, the tests may interact with your application via HTTP. Since the tests don’t need direct access to the classes under test in such cases, you don’t need to add the production classes to the test classpath.

## Configure the compilation and runtime classpaths for that source set

```kotlin
dependencies {
    // ...
    intTestImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    intTestRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}
```

## Create a task to run the integration tests

```kotlin
tasks {
    register<Test>("intTest") {
        description = "Runs integration tests."
        group = "verification"

        classpath = sourceSets["intTest"].runtimeClasspath
        testClassesDirs = sourceSets["intTest"].output.classesDirs

        useJUnitPlatform()
        testLogging.showStandardStreams = true

        shouldRunAfter("test")
    }

    named("check") { dependsOn("intTest") }
}
```
Now we can add everything together and run: `./gradlew intTest`. Few things:
* for each source sets we have matching dependency configurations:
	* main -> implementation, compileOnly, runtimeOnly
	* intTest -> intTestImplementation, intTestCompileOnly, intTestRuntimeOnly
* we can extend existing dependency configurations 
* we can define new tasks 
	* of types provided by a plugin (like `Test` type) 
	* bind them with custom source sets like "intTest"
 	* and get very specific behavior
