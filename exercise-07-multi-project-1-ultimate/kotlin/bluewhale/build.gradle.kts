// 7. Filtering via properties
extra["isFish"] = false

// 9. Add task only to specific projects and execute it from toplevel

tasks.register("distanceToIceberg") {
    doFirst {
        println("20 nautical miles")
    }
}