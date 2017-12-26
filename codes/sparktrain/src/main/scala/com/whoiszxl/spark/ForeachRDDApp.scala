package com.whoiszxl.spark

import java.sql.DriverManager

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 使用Spark Streaming完成词频统计,并将结果添加到MySQL
  */
object ForeachRDDApp {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("StatefulWordCount").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val lines = ssc.socketTextStream("120.78.72.189", 6666)

    val result = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)

    result.foreachRDD(rdd => {

      rdd.foreachPartition(partitionOfRecords => {
        val connection = createConnection()
        partitionOfRecords.foreach(record => {
          val sql = "insert into wordcount(word,word_count) values('" + record._1 + "','" + record._2 + "')"
          connection.createStatement().execute(sql)
        })
        connection.close()
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }

  /**
    * mysql 链接
    *
    * @return
    */
  def createConnection() = {
    Class.forName("com.mysql.jdbc.Driver");
    DriverManager.getConnection("jdbc:mysql://whoiszxl.com:3306/bigdata", "bigdata", "669731945");
  }

}
