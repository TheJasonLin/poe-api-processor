package com.poe.api.processor.entities
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonInt32, BsonString, BsonValue}

object PropertyFactory extends DocumentFactory[Property] {
  override def create(bsonValue: BsonValue, parent: Option[BsonValue]): Property = {
    val document: Document = bsonValue.asDocument()
    val name: String = document.get[BsonString]("name").get.getValue
    val values: Seq[Value] = parseValues(document)
    val displayMode: Int = document.get[BsonInt32]("displayMode").get.getValue
    val propertyType: Option[Int] = DocumentHelper.parseIntOption(document, "type")
    val progress: Option[Int] = DocumentHelper.parseIntOption(document, "progress")

    Property(name, values, displayMode, propertyType, progress)
  }

  private def parseValues(document: Document): Seq[Value] = {
    DocumentHelper.parseByKey[Value](document, ValueFactory, "values").get
  }
}
