package com.poe.api.processor

import com.poe.api.processor.entities.{ApiItem, Property}

object PropertyReader {
  def readMapTier(apiItem: ApiItem): Option[Int] = {
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
