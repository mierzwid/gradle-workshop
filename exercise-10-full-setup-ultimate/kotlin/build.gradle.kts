allprojects {
    apply(plugin = "base")
    version = project.properties["gradle-workshop.version"] ?: throw Exception("Version needs to be specified in gradle.properties")
    group = "com.example.workshop"
}
