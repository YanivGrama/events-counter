package com.yg.eventscounter.app

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.yg.eventscounter.domain.CountableEvent
import com.yg.eventscounter.domain.CountableEventType
import com.yg.eventscounter.domain.Event
import com.yg.eventscounter.domain.usecase.UpdateCountableEventUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import mu.KotlinLogging
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.Executors

private val logger = KotlinLogging.logger {}
private val updaterDispatcher = Executors.newFixedThreadPool(3).asCoroutineDispatcher()

@ExperimentalCoroutinesApi
fun CoroutineScope.rawEventsProducer(inputStream: InputStream) = produce<String>(context = Dispatchers.IO) {
    val br = BufferedReader(InputStreamReader(inputStream))
    for (msg in br.lines()) {
        send(msg)
    }
}

@ExperimentalCoroutinesApi
fun CoroutineScope.parsedEventsProducer(inbox: ReceiveChannel<String?>) = produce<Event>(context = Dispatchers.Default) {
    val gson = Gson()
    for (rawEvent in inbox) {
        try {
            send(gson.fromJson(rawEvent, Event::class.java))
        } catch (e: JsonSyntaxException) {
            logger.error { "failed to parse rawEvent: '$rawEvent', error: ${e.message}" }
        } catch (e: Exception) {
            logger.error { "failed to parse rawEvent: '$rawEvent', error: ${e.message}" }
        }
    }
}

@ExperimentalCoroutinesApi
fun CoroutineScope.countableEventsProducer(inbox: ReceiveChannel<Event>) = produce<CountableEvent>(context = Dispatchers.Default) {
    for (event in inbox) {
        event.event_type?.let { eventType ->
            send(CountableEvent(CountableEventType.Type, eventType))
        }
        event.data?.splitToWords()?.forEach { word ->
            send(CountableEvent(CountableEventType.Word, word))
        }
    }
}

fun CoroutineScope.statisticUpdaterActor(inbox: ReceiveChannel<CountableEvent>, updateCountableEventUseCase: UpdateCountableEventUseCase) = launch(context = updaterDispatcher) {
    for (countableEvent in inbox) {
        updateCountableEventUseCase.execute(countableEvent)
    }
}


private fun String.splitToWords(): List<String> {
    return replace('\n', ' ').split(" ")
}
