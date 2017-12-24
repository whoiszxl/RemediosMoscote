package com.whoiszxl.spark.kafka;

/**
 * Created by zxlvoid on 2017/12/24 0024.
 *  kafka api测试
 */
public class KafkaClientApp {
    public static void main(String[] args) {
        new KafkaProducer(KafkaProperties.TOPIC).start();

        new KafkaConsumer(KafkaProperties.TOPIC).start();
    }
}
