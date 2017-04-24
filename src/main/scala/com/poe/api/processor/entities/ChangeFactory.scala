package com.poe.api.processor.entities
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonObjectId, BsonString, BsonValue, ObjectId}

object ChangeFactory extends DocumentFactory[Change] {
  override def create(bsonValue: BsonValue, parent: Option[BsonValue]): Change = {
    val document: Document = bsonValue.asDocument()
    val _id: ObjectId = document.get[BsonObjectId]("_id").get.getValue
    val nextChangeId: String = document.get[BsonString]("nextChangeId").get.getValue
    val stashes: Seq[Stash] = parseStashes(document)

    Change(_id, nextChangeId, stashes)
  }

  private def parseStashes(document: Document): Seq[Stash] = {
    DocumentHelper.parseByKey[Stash](document, StashFactory, "stashes").get
  }
}
