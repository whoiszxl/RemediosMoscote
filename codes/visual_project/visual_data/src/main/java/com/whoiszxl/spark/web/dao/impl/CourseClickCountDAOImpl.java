package com.whoiszxl.spark.web.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.whoiszxl.spark.web.dao.ICourseClickCountDAO;
import com.whoiszxl.spark.web.entity.CourseClickCount;
import com.whoiszxl.spark.web.utils.HBaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CourseClickCountDAOImpl implements ICourseClickCountDAO{

	@Override
	public List<CourseClickCount> query(String days) throws Exception {
		List<CourseClickCount> list = new ArrayList<>();


        // 去HBase表中根据day获取实战课程对应的访问量
        Map<String, Long> map = HBaseUtils.getInstance().query("zxl_course_clickcount",days);

        for(Map.Entry<String, Long> entry: map.entrySet()) {
            CourseClickCount model = new CourseClickCount();
            model.setName(entry.getKey());
            model.setValue(entry.getValue());

            list.add(model);
        }

        return list;
	}

}
