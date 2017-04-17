package com.poe.api.processor

import com.mongodb.async.client.Observable
import com.poe.parser.item.Item
import org.mongodb.scala.{Completed, Document, MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Sorts._

object Database {
  // To directly connect to the default server localhost on port 27017
  private val mongoClient: MongoClient = MongoClient()
  private val database: MongoDatabase = mongoClient.getDatabase("poe")
  private val changeCollection: MongoCollection[Document] = database.getCollection("change")
  private val itemCollection: MongoCollection[Document] = database.getCollection("item")

  def nextChange: Observable[Document] = {
    changeCollection
      .find(equal("processed", false))
      .sort(ascending("time"))
      .first()
  }

  def insertItem(item: Item): Observable[Completed] = {
    val document: Document = ItemConverter.convert(item)
    itemCollection.insertOne(document)
  }

  def close(): Unit = {
    mongoClient.close()
  }
}
