package com.yg.eventscounter.domain.usecase

import com.yg.eventscounter.domain.CountableEventType
import com.yg.eventscounter.domain.repository.IMetricsRepository

class GetEventsStatusUseCase(private val metricsRepository: IMetricsRepository) {

    fun execute(countableEventType: CountableEventType): Map<String, Long> {
        return metricsRepository.getAllCounterValues(countableEventType)
    }
}