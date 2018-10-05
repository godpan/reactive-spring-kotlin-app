package me.pgs

import me.pgs.handler.StockHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.http.MediaType.TEXT_EVENT_STREAM
import org.springframework.web.reactive.function.server.RenderingResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.toMono

@Configuration
class Routes(val userHandler: StockHandler) {
    @Bean
    fun Router() = router {
        accept(MediaType.TEXT_HTML).nest {
            GET("/") { ok().render("index") }
        }
        "/api".nest {
            GET("/getStockQuotation").nest {
                accept(TEXT_EVENT_STREAM, userHandler::getStockQuotation)
            }
        }
        resources("/**", ClassPathResource("static/"))
    }.filter { request, next ->
        next.handle(request).flatMap {
            if (it is RenderingResponse) RenderingResponse.from(it).build() else it.toMono()
        }
    }
}