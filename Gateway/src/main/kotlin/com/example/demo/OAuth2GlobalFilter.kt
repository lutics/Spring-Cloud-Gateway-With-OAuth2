package com.example.demo

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class OAuth2GlobalFilter(
    private val jwtDecoder: ReactiveJwtDecoder
) : GlobalFilter, Ordered {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> = mono {
        val authorizationHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val accessToken = authorizationHeader.substring(7)

            val jwt = jwtDecoder.decode(accessToken).awaitSingle()

            logger.info("subject - ${jwt.subject}")

            val modifiedRequest = exchange.request.mutate()
                .header("X-Header-Customer-Id", jwt.subject)
                .build()

            val newExchange = exchange.mutate().request(modifiedRequest).build()

            chain.filter(newExchange).awaitFirstOrNull()
        } else {
            chain.filter(exchange).awaitFirstOrNull()
        }
    }

    override fun getOrder(): Int = -1
}