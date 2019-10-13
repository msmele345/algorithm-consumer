package com.mitchmele.messageconsumer.algorithmconsumer.kafka

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHandler
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

//this could replace the listen if added into integration flows or configs
class CountDownLatchHandler : MessageHandler {

    private val logger : Logger = LoggerFactory.getLogger(CountDownLatchHandler::class.java)

    private val latch: CountDownLatch = CountDownLatch(10)

    override fun handleMessage(message: Message<*>) {
        logger.info("received message='{}'", message)
        latch.countDown()
    }
}



/*

Simply put, a CountDownLatch has a counter field, which you can decrement as we require. We can then use it to block a calling thread until it's been counted down to zero.
If we were doing some parallel processing, we could instantiate the CountDownLatch with the same value for the counter as a number of threads we want to work across. Then, we could just call countdown() after each thread finishes, guaranteeing that a dependent thread calling await() will block until the worker threads are finished.

*/