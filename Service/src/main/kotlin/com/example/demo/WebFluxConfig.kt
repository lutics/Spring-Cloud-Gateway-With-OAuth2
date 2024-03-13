package com.example.demo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class WebFluxConfig {

    @Bean
    fun routerConfig(
        handler: DemoHandler
    ) = coRouter {
        GET("test", handler::test)
        GET("testSecured", handler::testSecured)
    }
}