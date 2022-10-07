package com.awaken.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object SparkSQL03_RDDAndDataSet {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //TODO 2 利用SparkConf创建sc对象
    val sc = new SparkContext(conf)
    val lineRDD: RDD[String] = sc.textFile("input\\user.txt")
    //普通rdd,数据只有类型,没有列名(缺少元数据)
    val rdd: RDD[(String, Long)] = lineRDD.map {
      line => {
        val fileds: Array[String] = line.split(",")
        (fileds(0), fileds(1).toLong)
      }
    }
    //利用SparkConf创建sparksession对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    //RDD和DF、DS转换必须要导的包(隐式转换),spark指的是上面的sparkSession
    import spark.implicits._

    //TODO RDD=>DS
    //普通rdd转DS,没办法补充元数据,因此一般不用
    val ds: Dataset[(String, Long)] = rdd.toDS()
    ds.show()

    //样例类RDD,数据是一个个的样例类,有类型,有属性名(列名),不缺元数据
    val userRDD: RDD[User] = rdd.map {
      t => {
        User(t._1, t._2)
      }
    }
    //样例类RDD转换DS,直接toDS转换即可,不需要补充元数据,因此转DS一定要用样例类RDD
    val userDs: Dataset[User] = userRDD.toDS()
    userDs.show()

    //TODO DS=>RDD
    //ds转成rdd,直接.rdd即可,并且ds不会改变rdd里面的数据类型
    val rdd1: RDD[(String, Long)] = ds.rdd
    val userRDD2: RDD[User] = userDs.rdd
    
    //TODO 4 关闭资源
    sc.stop()
  }
}