package com.awaken.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQL01_input {

    def main(args: Array[String]): Unit = {

        // 1 创建上下文环境配置对象
        val conf: SparkConf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")

        // 2 创建SparkSession对象
        val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

        // 3 读取数据
        val df: DataFrame = spark.read.json("input/user.json")

        // 4 可视化
        df.show()

        // 5 释放资源
        spark.stop()
    }
}