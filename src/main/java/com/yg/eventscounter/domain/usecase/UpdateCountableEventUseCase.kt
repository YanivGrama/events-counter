package com.yg.eventscounter.domain.usecase

import com.yg.eventscounter.domain.CountableEvent
import com.yg.eventscounter.domain.repository.IMetricsRepository

class UpdateCountableEventUseCase(private val metricsRepository: IMetricsRepository) {
    fun execute(event: CountableEvent) {
        metricsRepository.incCounter(event, 1)
    }
}