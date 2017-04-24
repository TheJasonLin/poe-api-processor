package com.poe.api.processor.entities
import java.util.Optional

import com.poe.constants.StashType
import com.typesafe.scalalogging.Logger
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonNull, BsonValue}

object StashFactory extends DocumentFactory[Stash] {
  val logger = Logger("Main")

  override def create(bsonValue: BsonValue): Stash = {
    val document: Document = bsonValue.asDocument()
    if(document.get("accountName").get.isInstanceOf[BsonNull]) {
      throw new IllegalArgumentException("No Name")
    }
    logger.info(s"[${document.get("accountName").toString}, ${document.get("id").toString}]")
    val accountName: String = document.get("accountName").get.asString().getValue
    val lastCharacterName: String = document.get("lastCharacterName").get.asString().getValue
    val id: String = document.get("id").get.asString().getValue
    val name: String = document.get("stash").get.asString().getValue
    val stashType: StashType = parseStashType(document)
    val items: Seq[ApiItem] = parseItems(document)
    val public: Boolean = document.get("public").get.asBoolean().getValue

    Stash(accountName, lastCharacterName, id, name, stashType, items, public)
  }

  private def parseStashType(document: Document): StashType = {
    val stashTypeString: String = document.get("stashType").get.asString().getValue
    val stashType: Optional[StashType] = StashType.getByKey(stashTypeString)
    if(stashType.isPresent) stashType.get()
    else throw new IllegalArgumentException("StashType not recognized: " + stashTypeString)
  }

  private def parseItems(document: Document): Seq[ApiItem] = {
    DocumentHelper.parseByKey[ApiItem](document, ApiItemFactory, "items").get
  }
}
