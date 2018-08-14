package com.clouway.loggingservice.adapter.gae.datastore

import com.clouway.loggingservice.core.Log
import com.clouway.pubsub.core.event.Event
import org.junit.Rule
import org.junit.Test
import rule.DatastoreRule
import org.hamcrest.CoreMatchers.`is` as Is
import org.junit.Assert.assertThat
import java.time.LocalDateTime

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class DatastoreLoggerTest {

    @Rule
    @JvmField
    val helper: DatastoreRule = DatastoreRule()

    data class TestEvent(val data: String) : Event

    private val testEvent = TestEvent("::data::")
    private val testTime = LocalDateTime.of(1, 1, 1, 1, 1, 1)
    private val testAfterTime = LocalDateTime.of(1, 1, 1, 1, 0, 1)
    private val testBeforeTime = LocalDateTime.of(1, 1, 1, 1, 2, 1)
    private val testLog = Log(testEvent, TestEvent::class.java, testTime)

    private val logger = DatastoreLogger()

    @Test
    fun shouldLogEventToDatastore(){
        logger.log(testEvent, TestEvent::class.java, testTime)
        
        assertThat(logger.getEventsFromExactTime(testTime).contains(testLog), Is(true))
    }

    @Test
    fun shouldReturnLoggedEventsInTimeRange(){
        logger.log(testEvent, TestEvent::class.java, testTime)
        logger.log(testEvent, TestEvent::class.java, testTime)

        assertThat(logger.getEventsFromTo(testAfterTime, testBeforeTime).contains(testLog), Is(true))
        assertThat(logger.getEventsFromTo(testAfterTime, testBeforeTime).size, Is(2))
    }
}