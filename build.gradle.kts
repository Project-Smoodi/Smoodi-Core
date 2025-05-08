plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "org.smoodi.core"

repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/Project-Smoodi/Smoodi-Core")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("TOKEN")
        }
    }
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

val slf4jVersion = "2.0.13"
val logbackVersion = "1.5.13"
val lombokVersion = "1.18.30"

configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "org.slf4j" && requested.name == "slf4j-api") {
                useVersion(slf4jVersion)
            }
            if (requested.group == "ch.qos.logback" && requested.name.startsWith("logback-")) {
                useVersion(logbackVersion)
            }
            if (requested.group == "org.projectlombok" && requested.name.startsWith("lombok")) {
                useVersion(lombokVersion)
            }
        }
    }
}

dependencies {

    // Smoodi
    api("org.smoodi.framework:docs-annotations:1.2.0")

    // Java
    implementation("org.reflections:reflections:0.10.2")

    // Jackson
    api("com.fasterxml.jackson.core:jackson-databind:2.13.5")

    // Logger
    api("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-core:$logbackVersion")

    // Lombok
    api("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // Test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-launcher:1.10.0")
}

tasks.test {
    useJUnitPlatform {
        excludeTags("forked")
    }

    finalizedBy(tasks.named("forkedTests"))
}

tasks.register<Test>("forkedTests") {
    useJUnitPlatform {
        includeTags("fork")
    }
    forkEvery = 1
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(
        listOf(
            "--add-exports", "jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED",
            "--add-exports", "jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
            "--add-exports", "jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED"
        )
    )
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {

    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "org.smoodi.framework"
            artifactId = "smoodi-core"
            version = "0.1.4-SNAPSHOT"

            artifact(tasks["sourcesJar"])

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
                        email.set("leetyxodud312@gmail.com")
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
            isAllowInsecureProtocol = true
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}