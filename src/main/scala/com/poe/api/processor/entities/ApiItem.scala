package com.poe.api.processor.entities

/**
  * @todo Verify that all relevant parameters are included
  */
case class ApiItem(
                    verified: Boolean,
                    w: Int,
                    h: Int,
                    ilvl: Int,
                    icon: String,
                    league: String,
                    id: String,
                    //@TODO: sockets
                    name: String,
                    typeLine: String,
                    identified: Boolean,
                    corrupted: Boolean,
                    lockedToCharacter: Boolean,
                    note: Option[String],
                    properties: Option[Seq[Property]],
                    additionalProperties: Option[Seq[Property]],
                    //@TODO: Requirements
                    implicitMods: Option[Seq[String]],
                    explicitMods: Option[Seq[String]],
                    frameType: Int,
                    inventoryId: String,
                    talismanTier: Option[Int],
                    // not included: socketedItems
                    // Passed Down
                    accountName: Option[String],
                    lastCharacterName: Option[String],
                    stashName: Option[String]
                  ) {

  def quality: Option[Int] = {
    if (properties.isEmpty) {
      return None
    }

    val qualityProperty: Option[Property] = properties
      .get
      .find((property: Property) => {
        property.name == "Quality"
      })

    if (qualityProperty.isEmpty) {
      return None
    }

    val qualityString: String = qualityProperty.get.values.head.value
    val qualityNumberString: String = qualityString.replaceAll("\\+", "").replaceAll("%", "")
    Option(qualityNumberString.toInt)
  }
}
