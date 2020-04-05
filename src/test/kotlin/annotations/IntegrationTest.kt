package it.czerwinski.home.monitoring.annotations

import org.junit.jupiter.api.Tag
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.lang.annotation.Inherited

@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
@OverrideAutoConfiguration(enabled = false)
@Transactional
@AutoConfigureCache
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
@SpringBootTest
@Tag("integration")
annotation class IntegrationTest
