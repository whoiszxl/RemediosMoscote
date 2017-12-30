package com.whoiszxl.spark.web.controller;

import com.whoiszxl.spark.web.dao.ICourseClickCountDAO;
import com.whoiszxl.spark.web.entity.CourseClickCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxlvoid on 2017/12/30 0030.
 */
@Controller
public class DataController {


    private static Map<String, String> courses = new HashMap<>();
    static {
        courses.put("112","红拂夜奔");
        courses.put("128","绿毛水怪");
        courses.put("145","朝花夕拾");
        courses.put("146","堂吉诃德");
        courses.put("131","双城记");
        courses.put("130","野草");
    }

    @Autowired
    ICourseClickCountDAO courseClickCountDAO;


    @RequestMapping(value = "/course_clickcount_dynamic", method = RequestMethod.POST)
    @ResponseBody
    public List<CourseClickCount> courseClickCount(String days) throws Exception {

        List<CourseClickCount> list = courseClickCountDAO.query(days);
        for(CourseClickCount model : list) {
            model.setName(courses.get(model.getName().substring(9)));
        }

        return list;
    }

    @RequestMapping(value = "/echarts", method = RequestMethod.GET)
    public String echarts(){
        return "data.html";
    }
}
