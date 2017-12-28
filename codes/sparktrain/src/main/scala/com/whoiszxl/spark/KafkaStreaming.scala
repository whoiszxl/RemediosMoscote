package com.whoiszxl.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Spark Streaming 对接Kafka
  */
object KafkaStreaming {

  def main(args: Array[String]): Unit = {

    if (args.length != 4) {
      System.err.println("params have mistakes")
    }

    var Array(zkQuorum, group, topics, numThreads) = args

    var sparkConf = new SparkConf() //.setAppName("KafkaReceiverWordCount").setMaster("local[2]")

    var ssc = new StreamingContext(sparkConf, Seconds(5))

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap

    val messages = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap)

    messages.map(_._2).count().print()

    ssc.start()
    ssc.awaitTermination()

  }
}
