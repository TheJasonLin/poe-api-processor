package com.poe.api.processor.entities

import org.mongodb.scala.bson.BsonValue

trait DocumentFactory[T] {
  def create(bsonValue: BsonValue, parent: Option[BsonValue] = None): T
}
