package com.clouway.loggingservice.adapter.spark

import com.clouway.loggingservice.core.Logger
import com.clouway.pubsub.core.event.UserTransactionEvent

/**
 * @author Tsvetozar Bonev (tsbonev@gmail.com)
 */
class UserTransactionHandler(logger: Logger)
    : AbstractEventHandler<UserTransactionEvent>(logger)