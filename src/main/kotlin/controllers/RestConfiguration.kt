package it.czerwinski.home.monitoring.controllers

import it.czerwinski.home.monitoring.model.converters.StringToSensorTypeConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class RestConfiguration : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(StringToSensorTypeConverter())
    }
}
