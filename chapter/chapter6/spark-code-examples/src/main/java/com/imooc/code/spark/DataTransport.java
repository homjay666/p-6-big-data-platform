package com.imooc.code.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

public class DataTransport {

    public static void main(String[] args) {
        //创建sparksession实例
        SparkSession sparksql = SparkSession.builder().appName("sparksql").enableHiveSupport().getOrCreate();
        //链接mysql 读取数据
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "imooc@123");
        properties.put("driver", "com.mysql.jdbc.Driver");

        Dataset<Row> dataset = sparksql.read().jdbc("jdbc:mysql://localhost:3306/imooc_dev",
                "(select * from dev_log) as t", properties);

        //使用dataframe相关的api 演示一下
        dataset.show();

        dataset.select("id","name","creator").show();

        dataset.printSchema();

        dataset.createOrReplaceTempView("testtable");
        //过滤数据

        Dataset<Row> result = sparksql.sql("select * from testtable where creator='hdfs'");
        //写入到db01 log_dev1 的hive表

        result.write().mode("append").format("Hive").saveAsTable("db01.log_dev1");

        sparksql.stop();
    }
}
