tasks.register("action") {
    doLast {
        println("Consuming message: ${rootProject.extra["producerMessage"]}")
    }
}

//2. Adding dependencies
// Exercise: make this build work - consumer consume message only after it is produced

tasks.named("action") {
    dependsOn(":producer:action")
}
