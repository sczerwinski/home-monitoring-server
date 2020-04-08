package it.czerwinski.home.monitoring.swagger

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    @Value("\${swagger.api.title}")
    private val title: String? = null

    @Value("\${swagger.api.description}")
    private val description: String? = null

    @Value("\${swagger.api.version}")
    private val version: String? = null

    @Value("\${swagger.api.base-url}")
    private val baseUrl: String? = null

    @Bean
    fun buildDocket(): Docket =
        Docket(DocumentationType.SWAGGER_2)
            .host(baseUrl)
            .select()
            .apis(RequestHandlerSelectors.basePackage("it.czerwinski.home.monitoring.controllers"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(buildApiInfo())

    private fun buildApiInfo(): ApiInfo =
        ApiInfoBuilder()
            .title(title)
            .description(description)
            .version(version)
            .build()
}