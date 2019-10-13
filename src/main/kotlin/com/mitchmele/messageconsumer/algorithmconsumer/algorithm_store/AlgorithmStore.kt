package com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store

import com.mitchmele.messageconsumer.algorithmconsumer.result.Result
import com.mitchmele.messageconsumer.algorithmconsumer.result.ServiceErrors
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import java.io.Serializable

data class BaseAlgorithm(
    val name: String = "",
    val codeSnippet: String = "",
    val category: String = CategoryDescription.EASY.value,
    val isSolved: Boolean = false
)

@Document(collection = "algorithmDomainModels")
data class AlgorithmDomainModel(
    @Id
    val name: String = "",
    val codeSnippet: String = "",
    val category: Category = Category(categoryDescription = ""),
    val isSolved: Boolean = true
) : Serializable


data class Category(
    val categoryDescription: String = "",
    val difficultlyLevel: Int = 1,
    val tags: List<Tag> = emptyList()
)

data class Tag(val label: String = "")

enum class CategoryDescription(val value: String) {
    HARD("HARD"),
    EASY("EASY"),
    MEDIUM("MEDIUM"),
    EXTREME_PROGRAMMING("EXTREME PROGRAMMING")
}


interface AlgorithmClient {
    fun saveAlgorithm(algorithm: AlgorithmDomainModel) : Result<Unit, ServiceErrors>
    fun findAlgorithmByName(algorithmName: String) : Result<AlgorithmDomainModel, ServiceErrors>
    fun deleteAlgorithmByName(algorithmName: String) : Result<Unit, ServiceErrors>
}

interface Gate {
    fun exchange(@Header(KafkaHeaders.TOPIC) topic: String, out: String)
}
