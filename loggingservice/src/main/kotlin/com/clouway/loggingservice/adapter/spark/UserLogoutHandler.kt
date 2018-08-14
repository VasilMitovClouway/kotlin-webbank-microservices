package com.clouway.loggingservice.adapter.spark

import com.clouway.loggingservice.core.Logger
import com.clouway.pubsub.core.event.UserLoggedOutEvent

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class UserLogoutHandler(logger: Logger)
    : AbstractEventHandler<UserLoggedOutEvent>(logger)