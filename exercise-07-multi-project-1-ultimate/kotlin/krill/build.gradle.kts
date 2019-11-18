// 5. Specific behaviour placed in projects script

tasks.named("hello") {
    doLast {
        println("- The weight of my species in summer is twice as heavy as all human beings.")
    }
}

// 7. Filtering via properties
extra["isFish"] = false

// 9. Add task only to specific projects and execute it from toplevel

tasks.register("distanceToIceberg") {
    doFirst {
        println("99 nautical miles")
    }
}