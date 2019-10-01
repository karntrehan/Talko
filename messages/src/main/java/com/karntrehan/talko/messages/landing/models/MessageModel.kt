package com.karntrehan.talko.messages.landing.models

open class MessageModel(val id: String) {
    fun longId(): Long {
        return try {
            id.toLong()
        } catch (t: Throwable) {
            id.hashCode().toLong()
        }
    }

    constructor(idInt: Int) : this(idInt.toString())
}