package com.poe.api.processor.entities
/**
  * @todo Verify that all relevant parameters are included
  */

case class ApiItem (
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
  properties: Option[Seq[Property]],
  additionalProperties: Option[Seq[Property]],
  //@TODO: Requirements
  implicitMods: Option[Seq[String]],
  explicitMods: Option[Seq[String]],
  frameType: Int,
  inventoryId: String,
  talismanTier: Option[Int]
  // not included: socketedItems
) {
  //@todo: Implement
  def quality: Option[Int] = None
}
