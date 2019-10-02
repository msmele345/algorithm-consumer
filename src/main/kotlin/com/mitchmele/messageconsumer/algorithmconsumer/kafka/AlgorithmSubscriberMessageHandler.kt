package com.mitchmele.messageconsumer.algorithmconsumer.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch


@Service
@Component
class AlgorithmSubscriberMessageHandler : AcknowledgingMessageListener<String, String> {

    private val logger = LoggerFactory.getLogger(AlgorithmSubscriberMessageHandler::class.java)

    override fun onMessage(data: ConsumerRecord<String, String>?, acknowledgment: Acknowledgment?) {
        logger.info(data.toString())
        acknowledgment?.let {
            it.acknowledge()
        }
    }


    val jsonMapper = ObjectMapper().apply {
        registerKotlinModule()
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        dateFormat = StdDateFormat()
    }

    private var latch: CountDownLatch = CountDownLatch(1)

    @KafkaListener(
        topics = ["algorithm_complete", "dummy"],
        groupId = "dummy",
        clientIdPrefix = "baller",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun getAlgorithms(algorithm: String ) {
        println("$$$$$$$$$$$$$$$$ $algorithm")
        val algo: Algorithm = jacksonObjectMapper().readValue(algorithm, Algorithm::class.java)
        println("ALGO: $algo")
        latch.countDown()
    }
}


data class Algorithm(
    val algorithmId: Int = 0,
    val name: String = "",
    val codeSnippet: String = "",
    val category: String = Category.EASY.value,
    val isSolved: Boolean = false
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

