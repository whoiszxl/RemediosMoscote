package com.whoiszxl.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 通过Spark Streaming处理文件系统
  */
object FileWordCount {

  def main(args: Array[String]): Unit = {

    //1.文件系统的只需要一个local就可以了
    val sparkConf = new SparkConf().setMaster("local").setAppName("FileWordCount")
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    //windows下的文件路径还有点问题
    val lines = ssc.textFileStream("file:///home/hadoop/aa")

    val result = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)

    result.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
