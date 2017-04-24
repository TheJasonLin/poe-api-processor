package com.poe.api.processor.entities

import org.mongodb.scala.bson.ObjectId


case class Change(
                   _id: ObjectId,
                   nextChangeId: String,
                   stashes: Seq[Stash]
                 )
