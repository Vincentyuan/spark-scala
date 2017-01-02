package com.tp.spark.core


import org.apache.spark.{SparkContext, SparkConf}

import org.apache.spark.rdd._
import com.tp.spark.utils.TweetUtils
import com.tp.spark.utils.TweetUtils._

/**
 *  We still use the dataset with the 8198 reduced tweets. The data are reduced tweets as the example below:
 *
 *  {"id":"572692378957430785",
 *  "user":"Srkian_nishu :)",
 *  "text":"@always_nidhi @YouTube no i dnt understand bt i loved of this mve is rocking",
 *  "place":"Orissa",
 *  "country":"India"}
 *
 *  We want to make some computations on the users:
 *  - find all the tweets by user
 *  - find how many tweets each user has
 */
object Ex1UserMining {

  val pathToFile = "data/reduced-tweets.json"

  /**
   *  Load the data from the json file and return an RDD of Tweet
   */
  def loadData(): RDD[Tweet] = {
    // Create the spark configuration and spark context
    val conf = new SparkConf()
        .setAppName("User mining")
        .setMaster("local[*]")
    conf.set("spark.driver.allowMultipleContexts", "true")
    val sc = new SparkContext(conf)

    // Load the data and parse it into a Tweet.
    // Look at the Tweet Object in the TweetUtils class.
    sc.textFile(pathToFile).mapPartitions(TweetUtils.parseFromJson(_))
  }

  /**
   *   For each user return all his tweets
   */
  def tweetsByUser(): RDD[(String, Iterable[Tweet])] = {
    val data =  Ex1UserMining.loadData
   // val result = RDD[(String,Iterable[Tweet])]

    //val iterable = Iterable[Tweet]()
    //if the id is the same then , add to the iterable.

    //data.map{e => (e.id,iterable)}//.reduceByKey{iterable += x}
    data.groupBy(e =>e.user)
  }

  /**
   *  Compute the number of tweets by user
   */
  def tweetByUserNumber(): RDD[(String, Int)] = {
    val data = Ex1UserMining.loadData()
    data.map(e => (e.user,1))reduceByKey(_ + _)
  }


  /**
   *  Top 10 twitterers
   */
  def topTenTwitterers(): Array[(String, Int)] = {

    val data = Ex1UserMining.tweetByUserNumber()

//    data.sortBy(_._2).top(10)
    data.sortBy(_._2,false).take(10)//top(10)
//    val sortby = data.sortBy(_._2,false)
//    sortby.foreach(e =>println("number 1:"+e._1+" nuber 2: "+e._2))
//    val top10 = sortby.take(10)
//    top10.foreach(e =>println("top 10 ,number 1:"+e._1+" number 2: "+e._2))
//    top10
  }

}

