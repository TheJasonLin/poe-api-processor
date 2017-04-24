package com.poe.api.processor.entities

import com.poe.constants.ValueType
import org.scalatest.{FlatSpec, Matchers}

class ApiItemSpec extends FlatSpec with Matchers {

  "quality" should "return Option(20)" in {
    val values: Seq[Value] = List(
      Value("+20%", ValueType.WHITE_PHYSICAL)
    )
    val properties: Seq[Property] = List(
      Property("Quality", values, 0)
    )
    val apiItem = ApiItem(true, 1, 1, 1, "", "", "", "", "", true, false, false, 0, "", properties = Option(properties))

    val quality: Option[Int] = apiItem.quality

    assert(quality.isDefined)
    assert(quality.get == 20)
  }

}
