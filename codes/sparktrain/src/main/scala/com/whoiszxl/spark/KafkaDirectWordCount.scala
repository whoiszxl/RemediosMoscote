package com.whoiszxl.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Spark Streaming 对接Kafka Direct
  */
object KafkaDirectWordCount {

  def main(args: Array[String]): Unit = {

    if (args.length != 2) {
      System.err.println("params have mistakes")
    }

    var Array(brokers,topics) = args

    var sparkConf = new SparkConf() //.setAppName("KafkaReceiverWordCount").setMaster("local[2]")

    var ssc = new StreamingContext(sparkConf, Seconds(5))

    val kafkaParams = Map[String,String]("metadata.broker.list"->brokers)

    val topicsSet = topics.split(",").toSet

    val messages = KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](ssc,kafkaParams,topicsSet)

    messages.map(_._2).flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).print()

    ssc.start()
    ssc.awaitTermination()

  }
}
