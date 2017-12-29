package com.whoiszxl.spark.project.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * HBASE操作工具类
 */
public class HBaseUtils {

    HBaseAdmin admin = null;
    Configuration configuration = null;

    private HBaseUtils() {
        configuration = new Configuration();
        configuration.set("hbase.zookeeper.quorum", "www.chenyuspace.com:2181");
        configuration.set("hbase.rootdir", "hdfs://www.chenyuspace.com:8020/hbase");

        try {
            admin = new HBaseAdmin(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HBaseUtils instance = null;

    public static synchronized HBaseUtils getInstance() {
        if (instance == null) {
            instance = new HBaseUtils();
        }
        return instance;
    }

    /**
     * 根据表名获取到实例
     *
     * @param tableName
     * @return
     */
    public HTable getTable(String tableName) {
        HTable table = null;
        try {
            table = new HTable(configuration, tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return table;
    }

    /**
     * 添加一条记录到hbase
     * @param tableName 表名
     * @param rowKey rowKey行
     * @param cf columnFamily
     * @param column 列
     * @param value 值
     */
    public void put(String tableName,String rowKey,String cf,String column,String value){
        HTable table = getTable(tableName);
        Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(cf),Bytes.toBytes(column),Bytes.toBytes(value));
        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //HTable table = getInstance().getTable("zxl_course_clickcount");
        //System.out.println(table.getName().getNameAsString());

        HBaseUtils.getInstance().put("zxl_course_clickcount","20171228_88","info","click_count","2");
    }

}
