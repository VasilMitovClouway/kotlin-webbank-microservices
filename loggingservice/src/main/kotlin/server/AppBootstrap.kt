package server

import com.clouway.loggingservice.adapter.gae.datastore.DatastoreLogger
import com.clouway.loggingservice.adapter.spark.UserLoginHandler
import com.clouway.loggingservice.adapter.spark.UserLogoutHandler
import com.clouway.loggingservice.adapter.spark.UserRegistrationHandler
import com.clouway.loggingservice.adapter.spark.UserTransactionHandler
import com.clouway.pubsub.core.event.*
import com.clouway.pubsub.factory.EventBusFactory
import com.google.cloud.ServiceOptions
import spark.Spark.post
import spark.servlet.SparkApplication

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class AppBootstrap : SparkApplication {
    override fun init() {

        val projectId = ServiceOptions.getDefaultProjectId()
        val logServiceUrl = "https://log-dot-$projectId.appspot.com"
        val pushUrl = "/_ah/push-handlers/pubsub/log"

        val logger = DatastoreLogger()

        val handlerMap = mapOf<Class<*>, EventHandler>(
                UserLoggedOutEvent::class.java to UserLogoutHandler(logger),
                UserRegisteredEvent::class.java to UserRegistrationHandler(logger),
                UserLoginEvent::class.java to UserLoginHandler(logger),
                UserTransactionEvent::class.java to UserTransactionHandler(logger)
        )

        val eventBus = EventBusFactory.createAsyncPubsubEventBus()

        eventBus.subscribe("user-change",
                "user-change-logging-service",
                "$logServiceUrl$pushUrl/message")

        post("$pushUrl/message", eventBus.register(handlerMap))
    }
}