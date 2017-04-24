package com.poe.api.processor

import com.mongodb.client.result.UpdateResult
import com.poe.api.processor.entities.{Change, ChangeFactory}
import com.poe.parser.item.{DBItem, Item, Mod}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.{Completed, Document, MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Database {
  // setup codec registries
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[DBItem]), fromRegistries(fromProviders(classOf[Mod])), DEFAULT_CODEC_REGISTRY)
  // To directly connect to the default server localhost on port 27017
  private val mongoClient: MongoClient = MongoClient()
  private val database: MongoDatabase = mongoClient.getDatabase("poe").withCodecRegistry(codecRegistry)
  private val changeCollection: MongoCollection[Document] = database.getCollection("change")
  private val itemCollection: MongoCollection[DBItem] = database.getCollection("item")

  def nextChange: Future[Change] = {
    changeCollection
      .find(equal("processed", false))
      .sort(ascending("time"))
      .head()
      .map((document: Document) => {
        ChangeFactory.create(document.toBsonDocument)
      })
  }

  def insertItem(item: Item): Future[Unit] = {
    val dBItem = item.asDBItem
    itemCollection
      .insertOne(dBItem)
      .head()
      .map((_: Completed) => {
        Unit
      })
  }

  def insertItems(items: Seq[Item]): Future[Unit] = {
    val documents: Seq[DBItem] = items.map(_.asDBItem).toSeq

    itemCollection
      .insertMany(documents)
      .head()
      .map((_: Completed) => {
        Unit
      })
  }

  def markChangeProcessed(id: ObjectId): Future[Boolean] = {
    changeCollection
      .updateOne(
        equal("_id", id),
        set("processed", true)
      )
      .head()
      .map((updateResult: UpdateResult) => {
        updateResult.wasAcknowledged()
      })
  }

  def close(): Unit = {
    mongoClient.close()
  }
}
