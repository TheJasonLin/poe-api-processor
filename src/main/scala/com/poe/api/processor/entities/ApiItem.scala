package com.poe.api.processor.entities

/**
  * @TODO: Verify that all relevant parameters are included
  */
class ApiItem(
             val verified: Boolean,
             val w: Int,
             val h: Int,
             val ilvl: Int,
             val icon: String,
             val league: String,
             val id: String,
             //@TODO: sockets
             val name: String,
             val typeLine: String,
             val identified: Boolean,
             val corrupted: Boolean,
             val lockedToCharacter: Boolean,
             val properties: Option[Set[Property]],
             //@TODO: Requirements
             val implicitMods: Set[String],
             val explicitMods: Set[String],
             val frameType: Int,
             val inventoryId: String
             // not included: socketedItems
             ) {
  def quality: Option[Int] = _
}
