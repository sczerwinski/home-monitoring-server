package it.czerwinski.home.monitoring.converters

import it.czerwinski.home.monitoring.model.converters.StringToSensorTypeConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class RestConvertersConfiguration : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(StringToSensorTypeConverter())
    }
}
