package com.poe.api.processor.entities

case class Property(
                     name: String,
                     values: Seq[Value],
                     displayMode: Int,
                     propertyType: Option[Int] = None,
                     progress: Option[Int] = None
                   )
