package com.poe.api.processor

import com.poe.api.processor.entities.Change
import com.poe.constants.Rarity
import com.poe.parser.KnownInfo
import com.poe.parser.item.{DivinationCard, Item}
import org.scalatest.{AsyncFlatSpec, Matchers}

class DatabaseSpec extends AsyncFlatSpec with Matchers {

  behavior of "nextChange"

  it should "get next change" in {
    Database.nextChange.map((change: Change) => {
      assert(change.nextChangeId.length > 0)
      assert(change.stashes.nonEmpty)
      assert(change.stashes.head.accountName.nonEmpty)

      succeed
    })
  }

  behavior of "insertItem"

  it should "not error" in {
    val item: Item = createDivCard("The Gambler")
    Database.insertItem(item).map((_) => {
      succeed
    })
  }

  behavior of "insertItems"

  it should "not error" in {
    val items: Seq[Item] = List(
      createDivCard("The Gambler"),
      createDivCard("The Fiend")
    )

    Database.insertItems(items).map((_) => {
      succeed
    })
  }


  private def createDivCard(name: String): DivinationCard = {
    val knownInfo: KnownInfo = new KnownInfo(name, Rarity.DIVINATION_CARD)
    new DivinationCard(knownInfo)
  }
}
