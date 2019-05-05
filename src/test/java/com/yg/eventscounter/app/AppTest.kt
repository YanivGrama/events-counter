package com.yg.eventscounter.app

import com.google.common.truth.Truth
import com.google.gson.Gson
import com.yg.eventscounter.domain.CountableEventType
import com.yg.eventscounter.domain.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream


class AppTest {
    private val app = AppFactory.createApp()

    @ExperimentalCoroutinesApi
    @Test
    fun `e2e test - with invalid json`() {
        runBlocking {
            val invalidEventsList = listOf("Some invalid Jsons", "sdfsfs")
            val eventsList = listOf(
                    Event("Text Msg 1", "Yaniv Grama", System.currentTimeMillis()),
                    Event("Text Msg 2", "Yaniv Bla", System.currentTimeMillis()),
                    Event(null, null, System.currentTimeMillis())
            ).toEventsJsonList()

            app.startConsumeEvents((invalidEventsList + eventsList).toInputStream())

            val testedWordsCount = app.getEventsStatus(CountableEventType.Word)
            Truth.assertThat(testedWordsCount).containsEntry("Yaniv", 2L)
            Truth.assertThat(testedWordsCount).containsEntry("Grama", 1L)
            Truth.assertThat(testedWordsCount).containsEntry("Bla", 1L)

            val testedTypesCount = app.getEventsStatus(CountableEventType.Type)
            Truth.assertThat(testedTypesCount).containsEntry("Text Msg 1", 1L)
            Truth.assertThat(testedTypesCount).containsEntry("Text Msg 2", 1L)
        }
    }
}

private fun List<String>.toInputStream(): InputStream {
    return ByteArrayInputStream(this.joinToString("\n").toByteArray(Charsets.UTF_8))
}

private fun List<Event>.toEventsJsonList(): List<String> {
    return this.map { Gson().toJson(it, Event::class.java) }
}






