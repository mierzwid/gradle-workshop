tasks.register("action") {
    doLast {
        println("Producting message:")
        rootProject.extra["producerMessage"] = "Watch the order of execution."
    }
}