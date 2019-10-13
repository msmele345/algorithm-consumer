package com.mitchmele.messageconsumer.algorithmconsumer.services

import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.AlgorithmDomainModel
import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.BaseAlgorithm
import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Tag
import com.mitchmele.messageconsumer.algorithmconsumer.utils.UnitTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.experimental.categories.Category


@Category(UnitTest::class)
class AlgorithmTransformerTest {


    private val subject = AlgorithmTransformer()

    @Test
    fun `transform - should map values from the baseAlgorithm input correctly to the DomainModel`() {

        val inputBaseAlgorithm = BaseAlgorithm(
            name = "capitalizeString",
            codeSnippet = """
                fun capitalizeString(str: String): String = str.toUpperCase() 
            """.trimIndent(),
            category = "EASY",
            isSolved = true
        )

        val expectedDomainModel = AlgorithmDomainModel(
            name = "capitalizeString",
            codeSnippet = """
                fun capitalizeString(str: String): String = str.toUpperCase() 
            """.trimIndent(),
            category = com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Category(
                categoryDescription = "EASY",
                difficultlyLevel = 2,
                tags = listOf(Tag("string manipulation"))
            )
        )

        val actual = subject.transform(inputBaseAlgorithm)
        assertThat(actual).isEqualTo(expectedDomainModel)
    }


    @Ignore
    @Test
    fun `parseCategoryInfo - should parse the name of the algorithm for key words and return a list of tags`() {
        val inputBaseAlgorithm = BaseAlgorithm(
            name = "sortArray",
            codeSnippet = """
                fun sortArray(array: List<Int>): List<Int> = array.sort() 
            """.trimIndent(),
            category = "EASY",
            isSolved = true
        )

        val expected = listOf(Tag("Arrays"))
        subject.parseCategoryInfo(inputBaseAlgorithm).let { actual ->
            assertThat(actual).isEqualTo(expected)
        }
    }
}



//val expectedDomainModel = AlgorithmDomainModel(
//    name = "sortArray",
//    codeSnippet = """
//                fun sortArray(array: List<Int>): List<Int> = array.sort()
//            """.trimIndent(),
//    category = com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Category(
//        categoryDescription = "EASY",
//        difficultlyLevel = 2,
//        tags = listOf(Tag("Arrays"))
//    )
//)