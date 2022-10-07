package com.awaken.sparkhive

import org.apache.spark.SparkConf
import org.apache.spark.sql._

object SparkSQL12_Hive {

    def main(args: Array[String]): Unit = {

        System.setProperty("HADOOP_USER_NAME","saberlind")

        // 1 创建上下文环境配置对象
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQLTest")
        // 2 创建SparkSession对象
        val spark: SparkSession = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()

        // 3 连接外部Hive，并进行操作
        spark.sql("drop table user3")
        spark.sql("show tables").show()
        spark.sql("create table user3(id int, name string)")
        spark.sql("insert into user3 values(1,'zs')")
        spark.sql("select * from user3").show

        // 4 释放资源
        spark.stop()
    }
}