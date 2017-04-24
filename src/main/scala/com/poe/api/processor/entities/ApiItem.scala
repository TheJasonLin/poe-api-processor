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
                    name: String,
                    typeLine: String,
                    identified: Boolean,
                    corrupted: Boolean,
                    lockedToCharacter: Boolean,
                    frameType: Int,
                    inventoryId: String,
                    note: Option[String] = None,
                    properties: Option[Seq[Property]] = None,
                    additionalProperties: Option[Seq[Property]] = None,
                    implicitMods: Option[Seq[String]] = None,
                    explicitMods: Option[Seq[String]] = None,
                    talismanTier: Option[Int] = None,
                    accountName: Option[String] = None,
                    lastCharacterName: Option[String] = None,
                    stashName: Option[String] = None
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
