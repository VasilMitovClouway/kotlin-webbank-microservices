package com.clouway.loggingservice.core

import com.clouway.pubsub.core.event.Event
import java.time.LocalDateTime

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
interface Logger {
    fun log(event: Event, eventType: Class<*>, time: LocalDateTime): Int
    fun getEventsFromExactTime(time: LocalDateTime): List<Log>
    fun getEventsFromTo(from: LocalDateTime, to: LocalDateTime): List<Log>
}