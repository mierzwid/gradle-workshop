tasks.register("action") {
    doLast {
        println("Consuming message: ${rootProject.extra["producerMessage"]}")
    }
}

//2. Adding dependencies

tasks.named("action") {
    dependsOn(":producer:action")
}
