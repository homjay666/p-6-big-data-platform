package com.imooc.code.spark.udf;


import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.types.DataTypes;

public class NagaUdf {
    //创建sparksession实例

    //创建自定义sparkudf

    //查询hive表，通过udf进行计算

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("udfTest").enableHiveSupport().getOrCreate();
        spark.udf().register("strLen", new UDF1<String, Integer>(){
            public Integer call(String str) throws Exception {
                return str.length();
            }
        }, DataTypes.IntegerType);

        spark.udf().register("toJson", new UDF2<String,String,String>(){
            public String call(String k,String v) throws Exception {
                return String.format("{\"%s\":\"%s\"}", k,v);
            }
        },DataTypes.StringType);

        spark.sql("select name,strLen(name) as len,toJson(creator,name) as obj from db01.log_dev1").show();

        spark.close();
    }
}
