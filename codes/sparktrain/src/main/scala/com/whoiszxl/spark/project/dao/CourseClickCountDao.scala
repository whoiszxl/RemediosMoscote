package com.whoiszxl.spark.project.dao

import com.whoiszxl.spark.kafka.utils.HBaseUtils
import com.whoiszxl.spark.project.entity.CourseClickCount
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

object CourseClickCountDao {

  val tableName = "zxl_course_clickcount"
  val cf = "info"
  val qualifer = "click_count"

  /**
    * 保存数据到hbase
    *
    * @param list
    */
  def save(list: ListBuffer[CourseClickCount]): Unit = {
    val table = HBaseUtils.getInstance().getTable(tableName)

    for (ele <- list) {
      table.incrementColumnValue(Bytes.toBytes(ele.day_course),
        Bytes.toBytes(cf),
        Bytes.toBytes(qualifer),
        ele.click_count)
    }
  }

  /**
    * 根据课程号查询
    *
    * @param day_course
    */
  def count(day_course: String): Long = {
    val table = HBaseUtils.getInstance().getTable(tableName)
    val get = new Get(Bytes.toBytes(day_course))
    val value = table.get(get).getValue(cf.getBytes, qualifer.getBytes)

    if (value == null) {
      0L
    } else {
      Bytes.toLong(value)
    }
  }

  def main(args: Array[String]): Unit = {
    var list = new ListBuffer[CourseClickCount]
    list.append(CourseClickCount("20170808_8", 8))
    list.append(CourseClickCount("20170808_9", 9))
    list.append(CourseClickCount("20170808_10", 10))

    save(list)
    print(count("20170808_8") + ":" + count("20170808_9") + ":" + count("20170808_10"))
  }
}
