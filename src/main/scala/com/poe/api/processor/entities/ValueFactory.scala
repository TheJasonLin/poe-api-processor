package com.poe.api.processor.entities
import java.util.Optional

import com.poe.constants.ValueType
import org.mongodb.scala.bson.{BsonArray, BsonValue}

object ValueFactory extends DocumentFactory[Value] {
  override def create(bsonValue: BsonValue): Value = {
    val array: BsonArray = bsonValue.asArray()
    if(array.size() != 2) {
      throw new IllegalArgumentException(s"Only array of size 2 allowed: ${array.size}")
    }
    val value: String = array.get(0).asString().getValue
    val valueType: ValueType = parseType(array)

    Value(value, valueType)
  }

  private def parseType(array: BsonArray): ValueType = {
    val key: Int = array.get(1).asInt32().getValue
    val value: Optional[ValueType] = ValueType.getByKey(key)
    if(value.isPresent) {
      value.get()
    } else {
      throw new IllegalArgumentException(s"Unrecognized key: $key")
    }
  }
}
