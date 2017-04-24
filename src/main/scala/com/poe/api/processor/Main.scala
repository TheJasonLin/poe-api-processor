package com.poe.api.processor

import com.poe.api.processor.entities.{ApiItem, Change, Property, Stash}
import com.poe.constants.Rarity
import com.poe.parser.item.Item
import com.poe.parser.{ItemFactory, KnownInfo}
import com.typesafe.scalalogging.Logger
import org.mongodb.scala.bson.ObjectId

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  val logger = Logger("Main")

  def main(args: Array[String]): Unit = {
    while(true) {
      val fFor: Future[Boolean] = for {
        nextChange: Change <- getNextChange
        items: Seq[Item] <-  parseItems(nextChange)
        stored: Boolean <- storeItems(items)
        done: Boolean <- markChangeProcessed(nextChange._id, stored)
      } yield done

      Await.result(fFor, Duration.Inf)
    }
  }

  private def getNextChange: Future[Change] = {
    Database.nextChange
  }

  private def parseItems(change: Change): Future[Seq[Item]] = {
    val items: Seq[Item] = change.stashes
      .flatMap((stash: Stash) => {
        stash.items
      })
      .map(createKnownInfo)
      .map((knownInfo: KnownInfo) => {
        ItemFactory.create(knownInfo)
      })

    Future.successful(items)
  }

  //@todo: Handle Errors
  private def storeItems(items: Seq[Item]): Future[Boolean] = {
    Database.insertItems(items).map((_) => {
      true
    })
  }

  private def createKnownInfo(apiItem: ApiItem): KnownInfo = {
    val rarity: Rarity = Rarity.values()(apiItem.frameType)
    val knownInfo: KnownInfo = new KnownInfo(apiItem.typeLine, rarity)
    knownInfo.id = Option(apiItem.id)
    knownInfo.name = if (apiItem.name.length > 0) Option(apiItem.name) else None
    knownInfo.itemLevel = Option(apiItem.ilvl)
    knownInfo.identified = Option(apiItem.identified)
    knownInfo.quality = apiItem.quality
    knownInfo.mapTier = readMapTier(apiItem)
    knownInfo.talismanTier = apiItem.talismanTier
    knownInfo.implicits = apiItem.implicitMods
    knownInfo.explicits = apiItem.explicitMods
    //@todo:  update here!!!
    knownInfo.accountName = apiItem.accountName
    knownInfo.lastCharacterName = apiItem.lastCharacterName
    knownInfo.stashName = apiItem.stashName

    knownInfo
  }

  private def markChangeProcessed(changeId: ObjectId, stored: Boolean): Future[Boolean] = {
    if(stored) {
      Database.markChangeProcessed(changeId)
    } else {
      logger.error("Storage Failed!")
      throw new IllegalStateException("Failed to store to database!")
    }
  }

  private def readMapTier(apiItem: ApiItem): Option[Int] = {
    if(apiItem.properties.isEmpty) return None

    val mapTierProperty = apiItem.properties.get.find((property: Property) => {
      property.name == "Map Tier"
    })

    if(mapTierProperty.isEmpty) return None

    val mapTier: Int =
      mapTierProperty
        .get
        .values
        .head
        .value
        .toInt

    Option(mapTier)
  }
}
