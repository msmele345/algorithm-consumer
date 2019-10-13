package com.mitchmele.messageconsumer.algorithmconsumer.services

import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.AlgorithmDomainModel
import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.BaseAlgorithm
import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Category
import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Tag
import org.springframework.integration.transformer.GenericTransformer
import org.springframework.stereotype.Service

@Service
class AlgorithmTransformer : GenericTransformer<BaseAlgorithm, AlgorithmDomainModel> {
    override fun transform(source: BaseAlgorithm): AlgorithmDomainModel {
        return AlgorithmDomainModel(
            name = source.name,
            codeSnippet = source.codeSnippet,
            category = Category(
                categoryDescription = source.category,
                difficultlyLevel = setDifficultyLevel(source.category),
                tags = listOf(Tag("Strings")) //TODO, input category description and return type

            )
        )
    }

    fun parseCategoryInfo(source: BaseAlgorithm): List<Tag> {
        val listOfCategoryNames = listOf("Strings", "Collections", "Numbers", "Sorting", "Processing", "Data formatting")

        val names = source.name.split("(?=[A-Z])")
        println(names)

        return emptyList()

    }
    fun setDifficultyLevel(str: String): Int {
        return when (str) {
            "EASY" -> 2
            "MEDIUM" -> 3
            "HARD" -> 4
            "EXTREME PROGRAMMING" -> 5
            else -> 1
        }
    }
}
