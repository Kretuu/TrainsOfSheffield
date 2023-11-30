plugins {
    id("java")
}

group = "uk.ac.sheffield.com2008"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("com.mysql:mysql-connector-j:8.2.0")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "uk.ac.sheffield.com2008.Main"
    }

    from(configurations.compileClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })
}

tasks.test {
    useJUnitPlatform()
}