var eventSource = new EventSource("/api/getStockQuotation");
eventSource.onmessage = function(e) {
    var li = document.createElement("li");
    var data = JSON.parse(e.data);
    li.innerText = "股票代码: " + data.stockQuotation.stock_id + " 股票名称:" + data.stockQuotation.stock_name + " 当前价格: " + (data.stockQuotation.price / 100.0).toFixed(2);
    document.getElementById("stockQuotations").appendChild(li);
}