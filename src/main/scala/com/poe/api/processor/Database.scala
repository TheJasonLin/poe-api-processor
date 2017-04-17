package com.poe.api.processor

import com.mongodb.async.client.Observable
import com.mongodb.client.result.UpdateResult
import com.poe.parser.item.Item
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.{Completed, Document, MongoClient, MongoCollection, MongoDatabase, result}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Updates._

import scala.concurrent.Future
import org.mongodb.scala

object Database {
  // To directly connect to the default server localhost on port 27017
  private val mongoClient: MongoClient = MongoClient()
  private val database: MongoDatabase = mongoClient.getDatabase("poe")
  private val changeCollection: MongoCollection[Document] = database.getCollection("change")
  private val itemCollection: MongoCollection[Document] = database.getCollection("item")

  def nextChange: Future[Document] = {
    changeCollection
      .find(equal("processed", false))
      .sort(ascending("time"))
      .head()
  }

  def insertItem(item: Item): Observable[Completed] = {
    val document: Document = Converter.convertItem(item)
    itemCollection.insertOne(document)
  }

  def markChangeProcessed(id: ObjectId): Future[UpdateResult] = {
    changeCollection
      .updateOne(
        equal("_id", id),
        set("processed", true)
      )
      .head()
  }

  def close(): Unit = {
    mongoClient.close()
  }
}
