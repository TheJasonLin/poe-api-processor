package com.poe.api.processor

import com.poe.api.processor.entities.Change
import com.poe.parser.item.Item
import org.mongodb.scala.bson.collection.immutable.Document

object Converter {
  def convertItem(item: Item): Document = _
  def convertChange(change: Document): Change = _
}
