package com.whoiszxl.spark.project.dao

import com.whoiszxl.spark.kafka.utils.HBaseUtils
import com.whoiszxl.spark.project.entity.{CourseClickCount, CourseSearchClickCount}
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

object CourseSearchClickCountDao {

  val tableName = "zxl_course_search_clickcount"
  val cf = "info"
  val qualifer = "click_count"

  /**
    * 保存数据到hbase
    *
    * @param list
    */
  def save(list: ListBuffer[CourseSearchClickCount]): Unit = {
    val table = HBaseUtils.getInstance().getTable(tableName)

    for (ele <- list) {
      table.incrementColumnValue(Bytes.toBytes(ele.day_search_course),
        Bytes.toBytes(cf),
        Bytes.toBytes(qualifer),
        ele.click_count)
    }
  }

  /**
    * 根据课程号查询
    *
    * @param day_search_course
    */
  def count(day_search_course: String): Long = {
    val table = HBaseUtils.getInstance().getTable(tableName)
    val get = new Get(Bytes.toBytes(day_search_course))
    val value = table.get(get).getValue(cf.getBytes, qualifer.getBytes)

    if (value == null) {
      0L
    } else {
      Bytes.toLong(value)
    }
  }

  def main(args: Array[String]): Unit = {
    var list = new ListBuffer[CourseSearchClickCount]
    list.append(CourseSearchClickCount("20171228_www.google.com_100", 100))
    list.append(CourseSearchClickCount("20171228_www.baidu.com_20", 20))
    list.append(CourseSearchClickCount("20171228_www.yahoo.com_50", 50))

    save(list)
    print(count("20171228_www.google.com_100") + ":" + count("20171228_www.baidu.com_20") + ":" + count("20171228_www.yahoo.com_50"))
  }
}
