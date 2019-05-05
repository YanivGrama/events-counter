package com.yg.eventscounter.domain.repository

import com.yg.eventscounter.domain.CountableEvent
import com.yg.eventscounter.domain.CountableEventType

interface IMetricsRepository {
    fun incCounter(countableEvent: CountableEvent, incBy: Long = 1)
    fun getAllCounterValues(typeCountable: CountableEventType): Map<String, Long>
}