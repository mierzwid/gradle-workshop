// A “lib” dependency is a special form of an execution dependency. It causes the other project to be built first
// and adds the jar with the classes of the other project to the classpath. It also adds the dependencies of the other
// project to the classpath. So you can enter the “api” directory and trigger a “gradle compile”. First the “shared”
// project is built and then the “api” project is built. Project dependencies enable partial multi-project builds.
