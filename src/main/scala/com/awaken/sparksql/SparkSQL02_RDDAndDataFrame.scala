package com.awaken.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object SparkSQL02_RDDAndDataFrame {
  def main(args: Array[String]): Unit = {
    // 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    // 利用SparkConf创建sc对象
    val sc = new SparkContext(conf)
    val lineRDD: RDD[String] = sc.textFile("input\\user.txt")
    //普通rdd,数据只有类型,没有列名(缺少元数据)
    val rdd: RDD[(String, Long)] = lineRDD.map {
      line => {
        val fileds: Array[String] = line.split(",")
        (fileds(0), fileds(1).toLong)
      }
    }
    // 利用SparkConf创建sparksession对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    //RDD和DF、DS转换必须要导的包(隐式转换),spark指的是上面的sparkSession
    import spark.implicits._

    //TODO RDD=>DF
    //普通rdd转换成DF,需要手动为每一列补上列名(补充元数据)
    val df: DataFrame = rdd.toDF("name", "age")

    df.show()

    //样例类RDD,数据是一个个的样例类,有类型,有属性名(列名),不缺元数据
    val userRDD: RDD[User] = rdd.map {
      t => {
        User(t._1, t._2)
      }
    }
    //样例类RDD转换DF,直接toDF转换即可,不需要补充元数据
    val userDF: DataFrame = userRDD.toDF()
    userDF.show()

    //TODO DF=>RDD
    //DF转换成RDD,直接.rdd即可,但是要注意转换出来的rdd数据类型会变成Row
    val rdd1: RDD[Row] = df.rdd
    val userRDD2: RDD[Row] = userDF.rdd
    rdd1.collect().foreach(println)
    userRDD2.collect().foreach(println)

    //如果想获取到row里面的数据,直接row.get(索引)即可
    val rdd2: RDD[(String, Long)] = rdd1.map {
      row => {
        (row.getString(0), row.getLong(1))
      }
    }

    rdd2.collect().foreach(println)

    // 关闭资源
    sc.stop()
  }
}
case class User(name:String,age:Long)