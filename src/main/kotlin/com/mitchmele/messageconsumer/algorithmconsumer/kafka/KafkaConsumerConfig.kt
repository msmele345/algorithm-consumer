package com.mitchmele.messageconsumer.algorithmconsumer.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.integration.dsl.Pollers
import org.springframework.integration.dsl.context.IntegrationFlowContext
import org.springframework.integration.kafka.dsl.Kafka
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.GenericMessageListenerContainer
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import java.time.Duration


@Configuration
@EnableKafka
class KafkaConsumerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Value("\${tbd.topic-name}")
    private lateinit var springIntegrationKafkaTopic: String


    @Bean
    fun consumerChannel(): PollableChannel {
        return QueueChannel()
    }

//    @Bean
//    fun kafkaMessageDrivenChannelAdapter(): KafkaMessageDrivenChannelAdapter<*, *> {
//        return KafkaMessageDrivenChannelAdapter(kafkaListenerContainerFactory()).apply {
//            setOutputChannel(consumerChannel())
//        }
//    }
    @Bean
    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<*> {                                     //switch back to string and add other config
        return ConcurrentKafkaListenerContainerFactory<String, Object>().apply {
            consumerFactory = consumerFactory()
            setMessageConverter(StringJsonMessageConverter())
            isBatchListener = true
        };
    }


    @Bean
    fun consumerFactory(): ConsumerFactory<String, Object> { //switch back to string
        return DefaultKafkaConsumerFactory(consumerConfigs())
    }

    @Bean
    fun consumerConfigs(): Map<String, Any> {
        val properties = HashMap<String, Any>()
        properties.run {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java) //try algorithm?
            put(ConsumerConfig.GROUP_ID_CONFIG, "dummy")
        }
        return properties
    }
}




//consumer configs have the configuration

//the consumer factory takes the consumer configs and a param

//the consumer factory bean w/configs is provided the the kafkaListerContainer to complete setup
