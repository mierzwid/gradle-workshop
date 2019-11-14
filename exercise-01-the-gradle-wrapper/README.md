# Exercise 1: The Gradle Wrapper

## Generating wrapper files

1. Go to `exercise-01-the-gradle-wrapper/kotlin`.
2. Run `gradle wrapper --gradle-version $CURRENT-VERSION --distribution-type all​`
    * where `$CURRENT-VERSION` is the [recent version released by Gradle community](https://gradle.org/releases/)

## Generating Gradle files

1. In the same directory, using wrapper:
    * run `./gradlew init`
    * select basic project type
    * kotlin as build script DSL
    * and gradle-workshop as the project name
    
Now we have an empty project with empty build files we can explore in the next exercises.

It is especially important that we used Wrapper, now we can rely on the version of Gradle we embedded into our project. All team members, if there would be a team, would use the same version fo Gradle.
