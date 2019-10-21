allprojects {
    apply(plugin = "base")
    group = "com.example.workshop"
    version = project.findProperty("version")
            ?: throw Exception("You need to configure application version")
}
