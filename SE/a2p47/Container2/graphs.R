library(lattice)

read.table("output-throughput-latency/stats.csv", header=TRUE,fill=T) -> csvDataFrameSource
csvDataFrame <- csvDataFrameSource

trellis.device("pdf", file="graph1.pdf", color=T, width=6.5, height=5.0)

xyplot(response ~ rate, data = csvDataFrame, xlab = "Requests", ylab = "Response Rate", type = "smooth")

dev.off() -> null 

trellis.device("pdf", file="graph2.pdf", color=T, width=6.5, height=5.0)

xyplot(latency ~ response, data = csvDataFrame, xlab = "Response Rate", ylab = "latency", type = "smooth")

dev.off() -> null

