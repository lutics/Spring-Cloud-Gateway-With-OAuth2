package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class DemoHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun test(
        serverRequest: ServerRequest
    ): ServerResponse {
        val customerId = serverRequest.headers().firstHeader("X-Header-Customer-Id")

        return ok().bodyValueAndAwait(
            mapOf(
                "customerId" to customerId
            )
        )
    }

    @PreAuthorize("#serverRequest.headers().firstHeader('X-Header-Customer-Id') != null")
    suspend fun testSecured(
        serverRequest: ServerRequest
    ): ServerResponse {
        val customerId = serverRequest.headers().firstHeader("X-Header-Customer-Id") as String

        return ok().bodyValueAndAwait(
            mapOf(
                "customerId" to customerId
            )
        )
    }
}
