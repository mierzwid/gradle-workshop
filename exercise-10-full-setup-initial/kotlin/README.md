# Gradle Full Setup Exercise

## Goal
The Goal of this exercise is to build those two projects together (despite the fact, that those are written using totally different technologies) and end up with one final artifacts.

## Step by step
1. We have two projects:  
a) backend - generated using: https://start.spring.io/#!language=kotlin&type=gradle-project - with Kotlin set as programming language and Gradle as a build tool  
b) frontend - generated using: https://create-react-app.dev/docs/getting-started - with REACT used as a JS framework

2. Let's verify our `backend` project builds properly by running  
a) `./gradlew build`  
b) `./gradlew bootRun`

3. Let's verify our `frontend` project builds properly by running  
a) `npm run build` - I can see some errors, since I have legacy version of Node.JS

4. Let's configure root project of multi-project build  
a) add `settings.gradle.kts` & `build.gradle.kts`  
b) move gradle wrapper from `backend` to top level (`backend/gradle`, `backend/gradlew*`) 

5. Let's configure [Gradle Plugin for Node](https://github.com/node-gradle/gradle-node-plugin) to avoid version problems and encapsulate our build environment.  
a) apply plugin  
b) configure node  
c) write `buildReact` task that will build our app  
d) let's build `./gradlew buildReact` and start `./gradlew npmStart` our app 

6. To end up with single artifact we need to make dependencies between `backend` & `frontend`  
a) let's see how `:backend:bootJar` is executed: `./gradlew bootJar --dry-run`  
b) then alter `:backend:bootJar` task to rely on `:frontend:buildReact`, e.g. hook into `:backned:processResources`  
c) we reveal specific names, what about more generic contract? let's apply `base` plugin and use high level tasks  
d) our performance is poor, we build frontend even when there are no changes, let's configure caching  
e) now we can use output artifacts from the `frontend` project in our `backend` tasks  

7. Let's configure single group and version for all projects in the root project.  
a) `allprojects` in main build file with group and version definitions  
b) add `gradle.properties` with our props definition  
c) and use it in `:backend:bootJar`

## Exercise 1:
Extend `backend:bootRun` task to open `localhost:8080` in your default browser.
```kotlin
import java.awt.Desktop
import java.net.URI

if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
    Desktop.getDesktop().browse(URI("http://localhost:8080"))
}
```

## Exercise 2:
Write `:backend:run` task that will run `:backend:bootJar` output artifact.
```kotlin
tasks.register<Exec>("run")
```