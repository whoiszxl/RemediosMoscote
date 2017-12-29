package com.whoiszxl.spark.project.spark

import com.whoiszxl.spark.project.dao.CourseClickCountDao
import com.whoiszxl.spark.project.entity.{ClickLog, CourseClickCount}
import com.whoiszxl.spark.project.utils.DataUtils
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.ListBuffer

/**
  * Created by zxlvoid on 2017/12/28 0028.
  * Spark Streaming 处理Kafka的数据
  */
object ZxlStreamingApp {

  def main(args: Array[String]): Unit = {

    if (args.length != 4) {
      System.err.print("params have mistake.must 4 param")
      System.exit(1)
    }

    val Array(zkQuorum, groupId, topics, numThreads) = args
    val sparkConf = new SparkConf().setAppName("ZxlStreamingApp").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(6))

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val messages = KafkaUtils.createStream(ssc, zkQuorum, groupId, topicMap)

    //1.测试数据接收
    //messages.map(_._2).count().print

    //2.数据清洗
    val logs = messages.map(_._2)
    //logs.print()
    val cleanData = logs.map(line => {
      val infos = line.split("\t")
      val url = infos(2).split(" ")(1)
      var courseId = 0

      //3.拿到课程编号
      if (url.startsWith("/class")) {
        val courseIdHTML = url.split("/")(2)
        courseId = courseIdHTML.substring(0, courseIdHTML.lastIndexOf(".")).toInt
      }
      ClickLog(infos(0), DataUtils.parseToMinute(infos(1)), courseId, infos(4).toInt, infos(3))

    }).filter(clickLog => clickLog.courseId != 0)

    cleanData.print()

    //3.统计今天到现在为止的课程访问量
    cleanData.map(x => {
      //将日期转换成 20171228_88格式
      (x.time.substring(0, 8) + "_" + x.courseId, 1)
    }).reduceByKey(_ + _).foreachRDD(rdd => {
      rdd.foreachPartition(partitionRecords => {
        val list = new ListBuffer[CourseClickCount]
        partitionRecords.foreach(pair => {
          list.append(CourseClickCount(pair._1, pair._2))
        })
        CourseClickCountDao.save(list)
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
