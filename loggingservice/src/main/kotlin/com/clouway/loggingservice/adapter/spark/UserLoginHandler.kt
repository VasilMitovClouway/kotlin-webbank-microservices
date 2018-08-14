package com.clouway.loggingservice.adapter.spark

import com.clouway.loggingservice.core.Logger
import com.clouway.pubsub.core.event.UserLoginEvent

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class UserLoginHandler(logger: Logger) : AbstractEventHandler<UserLoginEvent>(logger)