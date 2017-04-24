package com.poe.api.processor.entities

import org.mongodb.scala.bson.collection.immutable.Document
import org.scalatest.{FlatSpec, Matchers}

class DocumentHelperSpec extends FlatSpec with Matchers {
  "parseIntOption" should "return 5" in {
    val document = Document(
      "value" -> 5
    )

    val result = DocumentHelper.parseIntOption(document, "value")
    assert(result.isDefined)
    assert(result.get == 5)
  }

  "parseStringOption" should "return foxlin" in {
    val document = Document(
      "accountName" -> "foxlin"
    )

    val result = DocumentHelper.parseStringOption(document, "accountName")
    assert(result.isDefined)
    assert(result.get == "foxlin")
  }
}
