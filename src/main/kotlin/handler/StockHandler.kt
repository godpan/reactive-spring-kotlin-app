package me.pgs.handler

import me.pgs.service.StockService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.bodyToServerSentEvents

@Component
class StockHandler(val stockService: StockService) {

    fun getStockQuotation(req: ServerRequest) =
        ok().bodyToServerSentEvents(stockService.getStockQuotation())

}