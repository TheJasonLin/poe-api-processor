package com.poe.api.processor.entities

import org.mongodb.scala.bson.{BsonArray, BsonInt32, BsonString, BsonValue, Document}

import scala.collection.JavaConverters._
import scala.util.Try

object DocumentHelper {
  def parseByKey[T](document: Document, documentFactory: DocumentFactory[T], key: String): Option[Seq[T]] = {
    if (!document.contains(key)) {
      return None
    }

    val properties: Seq[T] = document
      .get[BsonArray](key)
      .get
      .getValues
      .asScala
      .map((bsonValue: BsonValue) => {
        Try(documentFactory.create(bsonValue, Option(document.toBsonDocument)))
      })
      .filter(_.isSuccess)
      .map(_.get)

    Option(properties)
  }

  def parseIntOption(document: Document, key: String): Option[Int] = {
    val value: Option[BsonInt32] = document.get[BsonInt32](key)
    if (value.isDefined) {
      Option(value.get.getValue)
    } else {
      None
    }
  }

  def parseStringOption(document: Document, key: String): Option[String] = {
    if(document.contains(key)) {
      Option(document.get[BsonString](key).get.getValue)
    } else {
      None
    }
  }
}
