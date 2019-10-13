package com.mitchmele.messageconsumer.algorithmconsumer.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.BaseAlgorithm
import com.mitchmele.messageconsumer.algorithmconsumer.mongodb.MongoClient
import com.mitchmele.messageconsumer.algorithmconsumer.services.AlgorithmTransformer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch



@Service
@Component
class AlgorithmMessageHandler(
    private val transformer: AlgorithmTransformer,
    private val mongoClient: MongoClient
) : AcknowledgingMessageListener<String, String> {

    private val logger = LoggerFactory.getLogger(AlgorithmMessageHandler::class.java)

    override fun onMessage(data: ConsumerRecord<String, String>?, acknowledgment: Acknowledgment?) {
        logger.info(data.toString())
        acknowledgment?.acknowledge()
    }

    private var latch: CountDownLatch = CountDownLatch(10)

    @KafkaListener(
        topics = ["algorithm_complete", "dummy"],
        groupId = "dummy",
        clientIdPrefix = "batch",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun getAlgorithms(@Payload algorithm: String,
                      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partitions: List<Integer>,
                      @Header(KafkaHeaders.OFFSET) offsets: List<Long>
    ) {

        val baseAlgorithm: BaseAlgorithm = createBaseAlgorithm(algorithm)
        println("ALGO: $baseAlgorithm")

        val algorithmDomainModel = transformer.transform(baseAlgorithm)
        println("DOMAIN MODEL: $algorithmDomainModel")


        mongoClient.saveAlgorithm(algorithmDomainModel).also {
            println("Successfully saved ${algorithmDomainModel.name}")
        }

        logger.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
        logger.info("beginning to consume message, offsets: $offsets, partitions: $partitions")
        latch.countDown()
    }

    private fun createBaseAlgorithm(algorithm: String): BaseAlgorithm {
        return jacksonObjectMapper().run {
            readValue(algorithm, BaseAlgorithm::class.java)
        }
    }
}
