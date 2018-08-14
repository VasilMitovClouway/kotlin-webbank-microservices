package com.clouway.loggingservice.adapter.spark

import com.clouway.loggingservice.core.Logger
import com.clouway.pubsub.core.event.UserRegisteredEvent
/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class UserRegistrationHandler(logger: Logger)
    : AbstractEventHandler<UserRegisteredEvent>(logger)