package com.mitchmele.messageconsumer.algorithmconsumer.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.channel.QueueChannel
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.messaging.PollableChannel
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.dsl.Pollers
import org.springframework.integration.dsl.context.IntegrationFlowContext
import org.springframework.integration.kafka.dsl.Kafka
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ErrorHandler
import org.springframework.kafka.listener.GenericMessageListenerContainer
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.SubscribableChannel
import java.lang.Exception
import java.time.Duration

@Configuration
@EnableKafka
class KafkaConsumerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Value("\${tbd.topic-name}")
    private lateinit var springIntegrationKafkaTopic: String


    @Bean
    fun consumerChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun errorQueue(): DirectChannel {
        return DirectChannel()
    }


    @Bean
    @ServiceActivator(inputChannel = "errorQueue", outputChannel = "consumerChannel")
    fun handleErrors(): ErrorHandler {
        println("ERRRRRRRROOOOOOOORRRRRRRRRRRR&&&&&&&")
        return ErrorHandler { thrownException, data ->
            println("ERROR MESSAGE")
            println(thrownException.localizedMessage)
            println("ERROR KEY")
            println(data.key())
            println("ERROR DATA")
            println(data.value())
            println("ERROR HEADERS")
            println(data.headers())
        }
    }

    @Bean
    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<*> {
        return ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            consumerFactory = consumerFactory()
            setErrorHandler(handleErrors())
//            setMessageConverter(StringJsonMessageConverter())
            isBatchListener = false
        }
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        return DefaultKafkaConsumerFactory(consumerConfigs())
    }

    @Bean
    fun consumerConfigs(): Map<String, Any> {
        val properties = HashMap<String, Any>()
        properties.run {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.GROUP_ID_CONFIG, "dummy")
        }
        return properties
    }
}
