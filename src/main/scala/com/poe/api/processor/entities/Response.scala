package com.poe.api.processor.entities

class Response(
              val nextChangeId: String,
              val stashes: Set[Stash],
              val time: Double,
              val processed: Boolean
              ) {


}
