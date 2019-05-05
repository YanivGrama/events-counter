package com.yg.eventscounter.data

import com.yg.eventscounter.domain.CountableEvent
import com.yg.eventscounter.domain.CountableEventType
import com.yg.eventscounter.domain.repository.IMetricsRepository

class MetricsRepositoryInMemory : IMetricsRepository {
    private val metricsMap = mutableMapOf<CountableEventType, MutableMap<String, Long>>()

    override fun incCounter(countableEvent: CountableEvent, incBy: Long) {
        val metricTypeMap = metricsMap.getOrPut(countableEvent.type) { mutableMapOf() }
        metricTypeMap[countableEvent.value] = metricTypeMap.getOrPut(countableEvent.value) { 0L } + incBy
    }


    override fun getAllCounterValues(typeCountable: CountableEventType): Map<String, Long> {
        return metricsMap.getOrElse(typeCountable) { emptyMap() }
    }
}