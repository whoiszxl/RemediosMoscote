package com.whoiszxl.spark.web;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.whoiszxl.spark.web.utils.HBaseUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApplicationTests {

	@Test
	public void contextLoads() throws Exception {
		Map<String, Long> map = HBaseUtils.getInstance().query("zxl_course_clickcount","20171229");

        for(Map.Entry<String, Long> entry: map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
	}

}
