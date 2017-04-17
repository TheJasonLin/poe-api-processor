package com.poe.api.processor

import com.mongodb.client.result.UpdateResult
import com.poe.api.processor.entities.{ApiItem, Change, Stash}
import com.poe.constants.Rarity
import com.poe.parser.{ItemFactory, KnownInfo}
import com.poe.parser.item.Item
import com.typesafe.scalalogging.Logger
import org.mongodb.scala.Document
import org.mongodb.scala.bson.ObjectId

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Main {
  val logger = Logger("Main")
  def main(args: Array[String]): Unit = {
    while(true) {
      val fFor = for {
        nextChange <- getNextChange
        items <-  parseItems(nextChange)
        done <- markChangeProcessed(nextChange.id, items)
      } yield done

      Await.result(fFor, Duration.Inf)
    }
  }

  private def getNextChange: Future[Change] = {
    Database.nextChange.map((changeDocument: Document) => {
      Converter.convertChange(changeDocument)
    })
  }

  private def parseItems(change: Change): Future[Set[Item]] = {
    val items: Set[Item] = change.stashes
      .flatMap((stash: Stash) => {
        stash.items
      })
      .map(createKnownInfo)
      .map((knownInfo: KnownInfo) => {
        ItemFactory.create(knownInfo)
      })
      .toSet[Item]

    Future.successful(items)
  }

  private def createKnownInfo(apiItem: ApiItem): KnownInfo = {
    val rarity: Rarity = Rarity.values()(apiItem.frameType)
    val knownInfo: KnownInfo = new KnownInfo(apiItem.typeLine, rarity)
    knownInfo.name = if (apiItem.name.length > 0) Option(apiItem.name) else None
    knownInfo.itemLevel = Option(apiItem.ilvl)
    knownInfo.identified = Option(apiItem.identified)
    knownInfo.quality = apiItem.quality

    //@TODO: Remaining Fields

    knownInfo
  }

  private def markChangeProcessed(changeId: ObjectId, items: Set[Item]): Future[UpdateResult] = {
    Database.markChangeProcessed(changeId)
  }
}
