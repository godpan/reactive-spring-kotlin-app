package me.pgs.models

import org.joda.time.DateTime

data class StockQuotation(
    val id: Long,
    val stock_id: Long, //股票代码
    val stock_name: String, //股票名称
    val price: Int,  //股票价格
    val time: String //当前时间
)