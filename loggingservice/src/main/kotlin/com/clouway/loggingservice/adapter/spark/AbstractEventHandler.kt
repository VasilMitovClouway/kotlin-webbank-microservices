package com.clouway.loggingservice.adapter.spark

import com.clouway.loggingservice.core.Logger
import com.clouway.pubsub.core.event.Event
import com.clouway.pubsub.core.event.EventHandler
import org.eclipse.jetty.http.HttpStatus
import spark.Request
import spark.Response
import java.time.LocalDateTime

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
@Suppress("UNCHECKED_CAST")
abstract class AbstractEventHandler<T : Event>(private val logger: Logger) : EventHandler {

    override fun handle(req: Request, res: Response): Any? {
        return try{
            val event = req.attribute<Event>("event") as T
            val time = req.attribute<LocalDateTime>("time")
            logger.log(event, javaClass, time)
        }catch (e: Exception){
            HttpStatus.NO_CONTENT_204
        }
    }
}