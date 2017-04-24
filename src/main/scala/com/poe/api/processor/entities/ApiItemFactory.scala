package com.poe.api.processor.entities

import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonBoolean, BsonInt32, BsonString, BsonValue}

import scala.collection.JavaConverters._

object ApiItemFactory extends DocumentFactory[ApiItem] {
  override def create(bsonValue: BsonValue, parent: Option[BsonValue]): ApiItem = {
    val document: Document = bsonValue.asDocument()
    val verified: Boolean = document.get[BsonBoolean]("verified").get.getValue
    val w: Int = document.get[BsonInt32]("w").get.getValue
    val h: Int = document.get[BsonInt32]("h").get.getValue
    val ilvl: Int = document.get[BsonInt32]("ilvl").get.getValue
    val icon: String = document.get[BsonString]("icon").get.getValue
    val league: String = document.get[BsonString]("league").get.getValue
    val id: String = document.get[BsonString]("id").get.getValue
    //@todo: sockets
    val name: String = document.get[BsonString]("name").get.getValue
    val typeLine: String = document.get[BsonString]("typeLine").get.getValue
    val identified: Boolean = document.get[BsonBoolean]("identified").get.getValue
    val corrupted: Boolean = document.get[BsonBoolean]("corrupted").get.getValue
    val lockedToCharacter: Boolean = document.get[BsonBoolean]("lockedToCharacter").get.getValue
    val note: Option[String] = DocumentHelper.parseStringOption(document, "note")
    val properties: Option[Seq[Property]] = parseProperties(document)
    val additionalProperties: Option[Seq[Property]] = parseAdditionalProperties(document)
    //@todo: Requirements
    val implicitMods: Option[Seq[String]] = parseImplicitMods(document)
    val explicitMods: Option[Seq[String]] = parseExplicitMods(document)
    val frameType: Int = document.get[BsonInt32]("frameType").get.getValue
    val inventoryId: String = document.get[BsonString]("inventoryId").get.getValue
    val talismanTier: Option[Int] = parseTalismanTier(document)
    // not included: socketedItems
    // Passed down attributes
    var accountName: Option[String] = None
    var lastCharacterName: Option[String] = None
    var stashName: Option[String] = None

    if(parent.isDefined) {
      val parentDocument: Document = parent.get.asDocument()
      accountName = DocumentHelper.parseStringOption(parentDocument, "accountName")
      lastCharacterName = DocumentHelper.parseStringOption(parentDocument, "lastCharacterName")
      stashName = DocumentHelper.parseStringOption(parentDocument, "stashName")
    }

    ApiItem(
      verified, w, h, ilvl, icon, league,
      id, name, typeLine, identified, corrupted,
      lockedToCharacter, note, properties,
      additionalProperties, implicitMods,
      explicitMods, frameType, inventoryId,
      talismanTier, accountName, lastCharacterName, stashName
    )
  }

  private def parseProperties(document: Document): Option[Seq[Property]] = {
    DocumentHelper.parseByKey[Property](document, PropertyFactory, "properties")
  }

  private def parseAdditionalProperties(document: Document): Option[Seq[Property]] = {
    DocumentHelper.parseByKey[Property](document, PropertyFactory, "additionalProperties")
  }

  private def parseImplicitMods(document: Document): Option[Seq[String]] = {
    parseModsByKey(document, "implicitMods")
  }

  private def parseExplicitMods(document: Document): Option[Seq[String]] = {
    parseModsByKey(document, "explicitMods")
  }

  private def parseModsByKey(document: Document, key: String): Option[Seq[String]] = {
    val mod = document.get[BsonArray](key)
    if(mod.isEmpty) {
      return None
    }

    val mods: Seq[String] = mod
      .get
      .getValues
      .asScala
      .map((bsonValue: BsonValue) => {
        bsonValue.asString().getValue
      })

    Option(mods)
  }

  private def parseTalismanTier(document: Document): Option[Int] = {
    DocumentHelper.parseIntOption(document, "talismanTier")
  }
}
