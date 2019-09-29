package com.mitchmele.messageconsumer.algorithmconsumer.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch


@Service
@Component
class AlgorithmSubscriber {

    private val logger = LoggerFactory.getLogger(AlgorithmSubscriber::class.java)

    private var latch: CountDownLatch = CountDownLatch(1)

    @KafkaListener(
        topics = ["algorithm_complete", "dummy"],
        groupId = "dummy",
        clientIdPrefix = "baller",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun getAlgorithms(algorithm: String) {
        val m: String = algorithm
        val algo: Algorithm = jacksonObjectMapper().readValue(m, Algorithm::class.java)
        println("STRING: $m")
        println("ALGO: $algo")
        println("$$$$$$$$$$$$$$$$ $algorithm")
        latch.countDown()
    }
}


data class Algorithm(
    val algorithmId: Int = 0,
    val name: String = "",
    val codeSnippet: String = "",
    val isSolved: Boolean = false,
    val category: String = Category.EASY.value
)


enum class Category(val value: String) {
    HARD("HARD"),
    EASY("EASY"),
    MEDIUM("MEDIUM"),
    EXTREME_PROGRAMMING("Extreme Programming")
}

interface Gate {
    fun exchange(@Header(KafkaHeaders.TOPIC) topic: String, out: String)
}