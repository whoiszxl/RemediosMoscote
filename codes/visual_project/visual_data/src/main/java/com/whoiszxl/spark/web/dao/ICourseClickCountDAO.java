package com.whoiszxl.spark.web.dao;

import java.util.List;

import com.whoiszxl.spark.web.entity.CourseClickCount;

public interface ICourseClickCountDAO {

	/**
     * 根据天查询
	 * @throws Exception 
     */
    public List<CourseClickCount> query(String days) throws Exception;
}
