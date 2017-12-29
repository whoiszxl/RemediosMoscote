package com.whoiszxl.spark.project.dao

import com.whoiszxl.spark.project.entity.CourseClickCount

import scala.collection.mutable.ListBuffer

class CourseClickCountDao {

  val tableName = "zxl_course_clickcount"
  val cf = "info"
  val qualifer = "click_count"

  /**
    * 保存数据到hbase
    *
    * @param list
    */
  def save(list: ListBuffer[CourseClickCount]): Unit = {

  }

  /**
    * 根据课程号查询
    * @param day_course
    */
  def count(day_course: String): Long = {
    0L
  }
}
