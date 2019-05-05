package com.yg.eventscounter.app

import com.yg.eventscounter.domain.CountableEventType
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.html.respondHtml
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.html.*


@ExperimentalCoroutinesApi
fun main() {
    val app = AppFactory.createApp()
    val server = embeddedServer(Netty, port = 8080) {
        launch {
            app.startConsumeEvents(System.`in`)
        }
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        routing {
            get("/") {
                call.respondHtml {
                    head {
                        title { +"Events Statistics" }
                    }
                    body {
                        h1 {
                            +"Events Statistics"
                        }
                        a("/wordsCount") {
                            +"Words Count"
                        }
                        br
                        a("/eventTypesCount") {
                            +"Event Types Count"
                        }
                    }
                }
            }
            get("/wordsCount") {
                call.respond(app.getEventsStatus(CountableEventType.Word))
            }

            get("/eventTypesCount") {
                call.respond(app.getEventsStatus(CountableEventType.Type))
            }
        }
    }
    server.start(wait = true)
}


