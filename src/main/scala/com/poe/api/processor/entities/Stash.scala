package com.poe.api.processor.entities

import com.poe.constants.StashType

case class Stash (
  accountName: String,
  lastCharacterName: String,
  id: String,
  name: String,
  stashType: StashType,
  items: Seq[ApiItem],
  public: Boolean
)