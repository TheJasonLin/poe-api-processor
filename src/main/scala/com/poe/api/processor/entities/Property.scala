package com.poe.api.processor.entities

class Property(
              val name: String,
              val values: Set[Value],
              val displayMode: Int,
              val propertiesType: Option[Int] = None,
              val progress: Option[Int] = None
              ) {

}
