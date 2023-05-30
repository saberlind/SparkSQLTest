package com.awaken.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql._

object SparkSQL09_Save {

  def main(args: Array[String]): Unit = {

    // 1 创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQLTest")

    System.setProperty("HADOOP_USER_NAME", "saberlind");
    // 脚本运行可以用以下参数
    // –conf "HADOOP_USER_NAME=bigdata"

    // 2 创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    // 3 获取数据
    val df: DataFrame = spark.read.json("file:///E:\\IDEAworkspace\\SparkSQLTest\\input\\user.json")

    // 4.1 df.write.保存数据：csv  jdbc   json  orc   parquet  text
    // 注意：保存数据的相关参数需写到上述方法中。如：text需传入加载数据的路径，JDBC需传入JDBC相关参数。
    // 默认保存为parquet文件（可以修改conf.set("spark.sql.sources.default","json")）
    df.write.save("file:///E:\\IDEAworkspace\\SparkSQLTest\\output")

    // 默认读取文件parquet
    spark.read.load("file:///E:\\IDEAworkspace\\SparkSQLTest\\output").show()

    // 4.2 format指定保存数据类型
    // df.write.format("…")[.option("…")].save("…")
    // format("…")：指定保存的数据类型，包括"csv"、"jdbc"、"json"、"orc"、"parquet"和"text"。
    // save ("…")：在"csv"、"orc"、"parquet"和"text"(单列DF)格式下需要传入保存数据的路径。
    // option("…")：在"jdbc"格式下需要传入JDBC相应参数，url、user、password和dbtable
    df.write.format("json").save("file:///E:\\IDEAworkspace\\SparkSQLTest\\output2")

    // 4.3 可以指定为保存格式，直接保存，不需要再调用save了
    df.write.json("file:///E:\\IDEAworkspace\\SparkSQLTest\\output1")

    // 4.4 如果文件已经存在则追加
    df.write.mode("append").json("file:///E:\\IDEAworkspace\\SparkSQLTest\\output2")

    // 如果文件已经存在则忽略(文件存在不报错,也不执行;文件不存在,创建文件)
    df.write.mode("ignore").json("file:///E:\\IDEAworkspace\\SparkSQLTest\\output2")

    // 如果文件已经存在则覆盖
    df.write.mode("overwrite").json("file:///E:\\IDEAworkspace\\SparkSQLTest\\output2")

    // 默认default:如果文件已经存在则抛出异常
    // path file:/E:/ideaProject2/SparkSQLTest/output2 already exists.;
    df.write.mode("error").json("file:///E:\\IDEAworkspace\\SparkSQLTest\\output2")

    // 5 释放资源
    spark.stop()
  }
}
