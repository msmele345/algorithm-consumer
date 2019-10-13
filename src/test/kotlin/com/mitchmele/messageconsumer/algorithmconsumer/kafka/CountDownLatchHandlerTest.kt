package com.mitchmele.messageconsumer.algorithmconsumer.kafka

import org.junit.Assert.*
import org.junit.Test

class CountDownLatchHandlerTest {

    val subject = CountDownLatchHandler()


    @Test
    fun `handleMessage - should block the thread until processing is complete`() {

    }

}