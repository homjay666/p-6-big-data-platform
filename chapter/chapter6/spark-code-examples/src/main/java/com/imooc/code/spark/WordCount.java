package com.imooc.code.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordCount {

    public static void main(String[] args) {
        //接收输入 input output
        String inputPath = args[0];
        String outputPath = args[1];
        //创建sparkcontext的实例

        SparkSession sparkSession = SparkSession.builder().appName("wordcount").getOrCreate();

        //获取Spark conf
        SparkConf conf = sparkSession.sparkContext().getConf();

        //设置相关的参数
        conf.set("spark.serializer","org.apache.spark.serializer.KryoSerializer");

        JavaSparkContext sc = new JavaSparkContext(sparkSession.sparkContext());

        //读取input下所有的文件
        JavaPairRDD<String, String> stringStringJavaPairRDD = sc.wholeTextFiles(inputPath).cache();


        stringStringJavaPairRDD.persist(StorageLevel.MEMORY_AND_DISK());


        JavaPairRDD<String, Integer> result = sc.wholeTextFiles(inputPath)
                .flatMap(file -> Arrays.asList(file._2.split("\\s+")).iterator())
                .mapToPair(word -> new Tuple2<String, Integer>(word, 1))
                .reduceByKey((a, b) -> a + b).persist(StorageLevel.MEMORY_AND_DISK());


        List<String> bigVal = new ArrayList<>();

        Broadcast<List<String>> broadcast = sc.broadcast(bigVal);

        broadcast.getValue();
        broadcast.destroy();


        //写出到output
        result.saveAsTextFile(outputPath);
        //关闭spark实例
        sparkSession.stop();
    }
}
