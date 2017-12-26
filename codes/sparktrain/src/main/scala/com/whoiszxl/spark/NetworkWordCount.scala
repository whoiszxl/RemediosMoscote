package com.whoiszxl.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * spark Streaming 处理sorket数据
  */
object NetworkWordCount {

  def main(args: Array[String]): Unit = {
    //1.创建一个spark配置类,配置本地运行，配置UI界面显示的name
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");

    //2.创建spark streaming的上下文对象,传入配置和批处理时间间隔为3秒
    val ssc = new StreamingContext(sparkConf,Seconds(3))

    //3.通过tcp获取到数据
    val lines = ssc.socketTextStream("120.78.72.189",6666)

    //4.通过空格进行分割
    val result = lines.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

    result.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
