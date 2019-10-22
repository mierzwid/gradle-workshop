allprojects {
    apply(plugin = "base")
    version = project.findProperty("version") ?: throw Exception("Version needs to be specified in gradle.properties")
    group = "com.example.workshop"
}
