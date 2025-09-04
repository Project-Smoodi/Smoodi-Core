import org.jreleaser.model.Active

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("org.jreleaser") version "1.17.0"
}

group = "org.smoodi.core"
version = "0.1.5-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    withJavadocJar()
    withSourcesJar()
}

val slf4jVersion = "2.0.13"
val logbackVersion = "1.5.13"
val lombokVersion = "1.18.34"

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
    api("org.smoodi.annotation:docs-annotations:1.3.0")

    // Java
    implementation("org.reflections:reflections:0.10.2")

    // Jackson
    api("com.fasterxml.jackson.core:jackson-databind:2.15.0")

    // Logger
    api("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-core:$logbackVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Lombok
    api("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // Test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-launcher:1.10.0")
}

tasks.test {
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStackTraces = true
        showCauses = true
        events("failed", "skipped")
    }

    useJUnitPlatform {
        excludeTags("fork")
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

publishing {

    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

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
            name = "staging"
            url = uri("${layout.buildDirectory}/staging-deploy")
        }
    }
}

jreleaser {
    signing {
        active.set(Active.RELEASE)
        armored = true
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active.set(Active.RELEASE)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    stagingRepository("${layout.buildDirectory}/staging-deploy")
                }
            }
            nexus2 {
                create("sonatype-snapshots") {
                    active.set(Active.SNAPSHOT)
                    url.set("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    snapshotUrl.set("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    applyMavenCentralRules.set(true)
                }
            }
        }
    }
    release {
        github {
            tagName.set("v{{projectVersion}}")
            releaseName.set("Release v{{projectVersion}}")
            changelog {
                formatted.set(Active.ALWAYS)
                preset.set("conventional-commits")
            }
        }
    }
}