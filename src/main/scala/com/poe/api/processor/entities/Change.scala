package com.poe.api.processor.entities

import org.mongodb.scala.bson.ObjectId

class Change(
            val id: ObjectId,
            val nextChangeId: String,
            val stashes: Seq[Stash]
            ) {


}
