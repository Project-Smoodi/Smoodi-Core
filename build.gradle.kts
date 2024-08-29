plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "org.smoodi.core"
version = "0.0.1-ALPHA"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

dependencies {

    // Java
    implementation("org.reflections:reflections:0.10.2")


    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")

    // Logger
    api("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-core:1.4.14")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(
        listOf(
            "--add-exports", "jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED",
            "--add-exports", "jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
            "--add-exports", "jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED"
        )
    )
}

publishing {

    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "org.smoodi.framework"
            artifactId = "smoodi-core"
            version = "0.0.1-ALPHA"

            pom {
                name.set("Smoodi Framework Core")
                description.set("The core module of Smoodi.")
                url.set("https://github.com/Project-Smoodi")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("Daybreak312")
                        name.set("Daybreak312")
                        email.set("ty82afg12@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/Project-Smoodi/Smoodi-Core.git")
                    developerConnection.set("scm:git:ssh://git@github.com:Project-Smoodi/Smoodi-Core.git")
                    url.set("https://github.com/Project-Smoodi/Smoodi-Core")
                }
            }
        }
    }

    repositories {
        maven {
            name = "Smoodi-Framework-Core"
            url = uri("https://maven.pkg.github.com/Project-Smoodi/Smoodi-Core")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}