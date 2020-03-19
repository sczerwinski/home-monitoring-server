package it.czerwinski.home.monitoring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HomeMonitoringServerApplication

fun main(args: Array<String>) {
    runApplication<HomeMonitoringServerApplication>(*args)
}
