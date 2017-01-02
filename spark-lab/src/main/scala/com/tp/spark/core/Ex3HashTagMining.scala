package com.tp.spark.core

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.rdd._
import com.tp.spark.utils.TweetUtils
import com.tp.spark.utils.TweetUtils._

/**
 *  We still use the dataset with the 8198 reduced tweets. Here an example of a tweet:
 *
 *  {"id":"572692378957430785",
 *    "user":"Srkian_nishu :)",
 *    "text":"@always_nidhi @YouTube no i dnt understand bt i loved of this mve is rocking",
 *    "place":"Orissa",
 *    "country":"India"}
 *
 *  We want to make some computations on the hashtags. It is very similar to the exercise 2
 *  - Find all the hashtags mentioned on a tweet
 *  - Count how many times each hashtag is mentioned
 *  - Find the 10 most popular Hashtag by descending order
 */
object Ex3HashTagMining {

  val pathToFile = "data/reduced-tweets.json"

  /**
   *  Load the data from the json file and return an RDD of Tweet
   */
  def loadData(): RDD[Tweet] = {
    // create spark configuration and spark context
    val conf = new SparkConf()
        .setAppName("Hashtag mining")
        .setMaster("local[*]")
    conf.set("spark.driver.allowMultipleContexts", "true")

    val sc = new SparkContext(conf)

    // Load the data and parse it into a Tweet.
    // Look at the Tweet Object in the TweetUtils class.
    sc.textFile(pathToFile)
        .mapPartitions(TweetUtils.parseFromJson(_))
  }

  /**
   *  Find all the hashtags mentioned on tweets
   */
  def hashtagMentionedOnTweet(): RDD[String] = {
//    ???
    val tweets = Ex3HashTagMining.loadData()
    tweets.flatMap(e => e.text.split(" ").filter(e => e.startsWith("#") && e.length >1).toList)
    }


  /**
   *  Count how many times each hashtag is mentioned
   */
  def countMentions(): RDD[(String, Int)] = {
//    ???
    val hashtag = Ex3HashTagMining.hashtagMentionedOnTweet()
    hashtag.map(e => (e,1)).reduceByKey(_ + _)
  }

  /**
   *  Find the 10 most popular Hashtags by descending order
   */
  def top10HashTags(): Array[(String, Int)] = {
    Ex3HashTagMining.countMentions().sortBy(_._2,false).take(10)
  }

}
