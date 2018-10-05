package me.pgs.service

import com.github.jasync.sql.db.RowData
import me.pgs.DB
import me.pgs.models.StockQuotation
import me.pgs.models.StockQuotationResult
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

@Component
class StockService(val db: DB) {

    val repeat = Flux.interval(Duration.ofMillis(1000)) //（1）

    fun getStockQuotation(): Flux<StockQuotationResult> {
        val query = "select * from stock_quotation order by id desc limit 1;"
        fun stockQuotation(time: DateTime) = Mono.fromFuture(db.connectionPool.sendPreparedStatement(query)).map {//（2）
            StockQuotationResult(time.toString("YYYY-MM-dd hh:mm:ss"), transRowDataToStockQuotation(it.rows.orEmpty().first())) //（3）
        } //（5）
        return repeat.flatMap {
            insertStockQuotation()
            stockQuotation(DateTime.now()) }
    }

    private fun transRowDataToStockQuotation(rowData: RowData): StockQuotation { //（4）
        return StockQuotation(
                rowData.get("id").toString().toLong(),
                rowData.get("stock_id").toString().toLong(),
                rowData.get("stock_name").toString(),
                rowData.get("price").toString().toInt(),
                (rowData.get("time") as LocalDateTime).toString("YYYY-MM-dd hh:mm:ss"))
    }

    private fun insertStockQuotation() {
        val max = 74000
        val min = 72000
        val price = Random().nextInt(max - min) + min
        val query = "insert into stock_quotation (stock_id, stock_name, price, time) values (600519, '贵州茅台', ${price}, '${DateTime.now().toString("YYYY-MM-dd hh:mm:ss")}')"
        db.connectionPool.sendPreparedStatement(query)
    }
}