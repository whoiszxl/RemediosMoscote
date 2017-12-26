package com.whoiszxl.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.flume.FlumeUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by zxlvoid on 2017/12/26 0026.
  * Spark Streaming整合Flume的第一种方式
  */
object FlumePushWordCount {

  def main(args: Array[String]): Unit = {

    if (args.length != 2) {
      System.err.println("params is required")
      System.exit(1)
    }

    val Array(hostname, port) = args

    val sparkConf = new SparkConf()//.setMaster("local[2]").setAppName("FlumePushWordCount")
    val ssc = new StreamingContext(sparkConf, Seconds(4))

    val flumeStream = FlumeUtils.createStream(ssc, hostname, port.toInt)

    flumeStream.map(x => new String(x.event.getBody.array()).trim)
      .flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).print()

    ssc.start()
    ssc.awaitTermination()
  }

}
