package com.yg.eventscounter.domain

data class Event(val event_type: String?, val data: String?, val timestamp: Long?)

data class CountableEvent(val type: CountableEventType, val value: String)

enum class CountableEventType {
    Type, Word
}
