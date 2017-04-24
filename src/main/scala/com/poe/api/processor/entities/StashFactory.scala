package com.poe.api.processor.entities

import java.util.Optional

import com.poe.constants.StashType
import com.typesafe.scalalogging.Logger
import org.mongodb.scala.bson.{BsonBoolean, BsonNull, BsonString, BsonValue, Document}

object StashFactory extends DocumentFactory[Stash] {
  val logger = Logger("Main")

  override def create(bsonValue: BsonValue, parent: Option[BsonValue]): Stash = {
    val document: Document = bsonValue.asDocument()
    if (document.get("accountName").get.isInstanceOf[BsonNull]) {
      throw new IllegalArgumentException("No Name")
    }
    logger.info(s"Account: ${document.get("accountName").toString}")
    val accountName: String = document.get[BsonString]("accountName").get.getValue
    val lastCharacterName: String = document.get[BsonString]("lastCharacterName").get.getValue
    val id: String = document.get[BsonString]("id").get.getValue
    val name: String = document.get[BsonString]("stash").get.getValue
    val stashType: StashType = parseStashType(document)
    val items: Seq[ApiItem] = parseItems(document)
    val public: Boolean = document.get[BsonBoolean]("public").get.getValue

    Stash(accountName, lastCharacterName, id, name, stashType, items, public)
  }

  private def parseStashType(document: Document): StashType = {
    val stashTypeString: String = document.get("stashType").get.asString().getValue
    val stashType: Optional[StashType] = StashType.getByKey(stashTypeString)
    if (stashType.isPresent) stashType.get()
    else throw new IllegalArgumentException("StashType not recognized: " + stashTypeString)
  }

  private def parseItems(document: Document): Seq[ApiItem] = {
    DocumentHelper.parseByKey[ApiItem](document, ApiItemFactory, "items").get
  }
}
