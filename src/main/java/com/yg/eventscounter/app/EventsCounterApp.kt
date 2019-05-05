package com.yg.eventscounter.app

import com.yg.eventscounter.data.MetricsRepositoryInMemory
import com.yg.eventscounter.domain.CountableEventType
import com.yg.eventscounter.domain.repository.IMetricsRepository
import com.yg.eventscounter.domain.usecase.GetEventsStatusUseCase
import com.yg.eventscounter.domain.usecase.UpdateCountableEventUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import java.io.InputStream

private const val numOfActorsInParallel = 5

object AppFactory {
    private val metricsRepository: IMetricsRepository = MetricsRepositoryInMemory()
    private val getEventsStatusUseCase = GetEventsStatusUseCase(metricsRepository)
    private val updateEventStatusUseCase = UpdateCountableEventUseCase(metricsRepository)
    fun createApp(): IEventsCounterApp {
        return EventsCounterApp(getEventsStatusUseCase, updateEventStatusUseCase)
    }
}

interface IEventsCounterApp {
    suspend fun startConsumeEvents(inputStream: InputStream)
    fun getEventsStatus(countableEventType: CountableEventType): Map<String, Long>
}

class EventsCounterApp(private val getEventsStatusUseCase: GetEventsStatusUseCase,
                       private val updateCountableEventUseCase: UpdateCountableEventUseCase) : IEventsCounterApp {

    @ExperimentalCoroutinesApi
    override suspend fun startConsumeEvents(inputStream: InputStream) {
        coroutineScope {
            val rawEventsProducer = rawEventsProducer(inputStream)
            val parsedEventsProducer = parsedEventsProducer(rawEventsProducer)
            val countableEventsProducer = countableEventsProducer(parsedEventsProducer)
            repeat(numOfActorsInParallel) { statisticUpdaterActor(countableEventsProducer, updateCountableEventUseCase) }
        }
    }

    override fun getEventsStatus(countableEventType: CountableEventType): Map<String, Long> {
        return getEventsStatusUseCase.execute(countableEventType)
    }

}