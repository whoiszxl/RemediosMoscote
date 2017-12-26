package com.whoiszxl.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 黑名单过滤
  */
object TransFormApp {

  def main(args: Array[String]): Unit = {
    //1.创建一个spark配置类,配置本地运行，配置UI界面显示的name
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");

    //2.创建spark streaming的上下文对象,传入配置和批处理时间间隔为3秒
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    /**
      * 构建一个黑名单
      */
    val blacks = List("zhouer", "wangwu");
    val blacksRDD = ssc.sparkContext.parallelize(blacks).map(x => (x, true))


    //3.通过tcp获取到数据
    val lines = ssc.socketTextStream("120.78.72.189", 6666)

    val clicklog = lines.map(x => (x.split(",")(1), x)).transform(rdd => {
      rdd.leftOuterJoin(blacksRDD).filter(x => x._2._2.getOrElse(false) != true)
        .map(x => x._2._1)
    })

    clicklog.print()

    ssc.start()
    ssc.awaitTermination()
  }

}
