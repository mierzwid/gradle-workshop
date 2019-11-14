# Exercise 3: Kotlin intro

This will be quick dive into Kotlin programming language from the perspective of Gradle user. We will show some concepts used in Kotlin that are crucial for Gradle and probably made Gradle developers choose this language as a new one side by side with Groovy.

For more complete information please use [Kotlin Language docs](https://kotlinlang.org/docs/reference/) 

## General - JVM language

Kotlin is a JVM language, which means that all the Java libraries are also available in Kotlin. Let's see:

```kotlin
println("Hello World!")

System.out.println("Hello World!")
``` 

The following two lines of code are equivalent. `println` is just a shortcut in Kotlin. We can simply invoke any function or instantiate an object from Java in Kotlin.

## Types and mutability

Kotlin is a statically typed language. That means, all the types are known at the compile time. However, it greatly extends/alters what we got used to in Java:

### Mutability

See: https://kotlinlang.org/docs/reference/basic-syntax.html#defining-variables

```kotlin
val immutable = "immutable value" // immutable = "new value" - won't compile
var mutable = "mutable"
mutable = "new value"
println("Printing values: $immutable, $mutable")
```

### Nullable types

See: https://kotlinlang.org/docs/reference/basic-syntax.html#using-nullable-values-and-checking-for-null

```kotlin
val nullableString: String? = null
val string: String = nullableString ?: "default value"
println("Printing nullable type: ${nullableString ?: "default value"}")
```

## Functions & return statements
See: https://kotlinlang.org/docs/reference/functions.html#functions

```kotlin

fun sayHelloWorld() {
    println("Function Hello World")
}

sayHelloWorld()

fun createMessage() { // in functions we need to return result explicitly
    "The last expression is returned from a function"
}

println(createMessage()) // kotlin.Unit

val createMessageLambda = {
    // the last expression in lambda is returned
    "The last expression is returned from a function"
}

println(createMessageLambda()) // Last expression is returned from a lambda
```

## Classes & objects

See: https://kotlinlang.org/docs/reference/classes.html#classes-and-inheritance

```kotlin
class AEM(val company: String = "Adobe", val version: String) {  // final by default
    fun fullName() = "AEM $version made by $company" // short notation -> return type inferred
}

val aem = AEM(version = "6.5") // new keyword omitted

println(aem.fullName()) // props are public by default
```

## Lambdas

See: https://kotlinlang.org/docs/reference/lambdas.html

```kotlin
val echoIt: (Any) -> Unit = {
    // need to specify type explicitly since it cannot be inferred otherwise
    println("Echoing it: $it")
}
echoIt(true)
```

## Extension functions

See: https://kotlinlang.org/docs/reference/extensions.html#extension-functions

```kotlin
fun AEM.prettyName() {
    println("Pretty: AEM $version made by $company")
}

aem.prettyName()
```

## Gradle utilizing Kotlin - lets mimic Application plugin

See: https://kotlinlang.org/docs/reference/lambdas.html#function-literals-with-receiver

```kotlin
// Function literals with receiver: 
class ApplicationPlugin(var mainClassName: String = "", var applicationDefaultJvmArgs: List<String> = listOf()) {
    fun run() {
        println("java -cp build/distributions/app.jar $mainClassName " + applicationDefaultJvmArgs.joinToString(" "))
    }
}

val plugin = ApplicationPlugin()

fun application(configurer: ApplicationPlugin.() -> Unit) {
    val plugin = ApplicationPlugin().apply(configurer)
    plugin.run()
}

application {
    mainClassName = "com.cognified.Main"
    applicationDefaultJvmArgs = listOf("-Xms2G", "-Xmx2G")
}
```

This is just the tip of the iceberg of the Kotlin language. Please refer to other sources to learn more.

## For volunteers: Write code enabling to create HTML DSL - body and div tags

```kotlin
val document = html {
    body {
        div {
            attribute("id", "id-header")
            attribute("class", "header")
        }
        div {
            attribute("id", "id-content")
            attribute("class", "content")
        }
        div {
            attribute("id", "id-footer")
            attribute("class", "footer")
        }
    }
}

println(document)
```


