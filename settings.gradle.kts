rootProject.name = "home-monitoring-server"

pluginManagement {

    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        id("org.springframework.boot") version "2.2.5.RELEASE"
        id("io.spring.dependency-management") version "1.0.9.RELEASE"
    }
}
