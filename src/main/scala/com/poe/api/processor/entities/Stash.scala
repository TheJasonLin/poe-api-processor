package com.poe.api.processor.entities

class Stash(
           val accountName: String,
           val lastCharacterName: String,
           val id: String,
           val name: String,
           val stashType: StashType,
           val items: Set[ApiItem],
           val public: Boolean
           ) {

}
