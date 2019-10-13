package com.mitchmele.messageconsumer.algorithmconsumer.debug

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.AlgorithmDomainModel
import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Tag
import com.mitchmele.messageconsumer.algorithmconsumer.mongodb.AlgorithmMongoRepository
import com.mitchmele.messageconsumer.algorithmconsumer.utils.UnitTest
import com.nhaarman.mockito_kotlin.mock
import no.skatteetaten.aurora.mockmvc.extensions.ExactPath
import no.skatteetaten.aurora.mockmvc.extensions.get
import no.skatteetaten.aurora.mockmvc.extensions.statusIsOk
import org.junit.Test
import org.junit.experimental.categories.Category
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@Category(UnitTest::class)
class DebugControllerTest {

    val mockRepo: AlgorithmMongoRepository = mock()

    val subject = DebugController(mockRepo)

    val mockMvc = MockMvcBuilders
        .standaloneSetup(subject)
        .build()


    @Test
    fun `showAllAlgorithms - should return a responseEntity showing all current algos in mongodb`() {

        val expectedResponse = listOf(
            listOf(AlgorithmDomainModel(
                name = "capitalizeString",
                codeSnippet = """
                fun capitalizeString(str: String): String = str.toUpperCase() 
            """.trimIndent(),
                category = com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Category(
                    categoryDescription = "EASY",
                    difficultlyLevel = 2,
                    tags = listOf(Tag("string manipulation"))
                )
            ),
                AlgorithmDomainModel(
                    name = "sortNumberArray",
                    codeSnippet = """
                fun sortArray(array: List<Int>): List<Int> = array.sort() 
            """.trimIndent(),
                    category = com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Category(
                        categoryDescription = "EASY",
                        difficultlyLevel = 2,
                        tags = listOf(Tag("Collections"))
                    )
                ))
        )

        mockMvc.get(pathBuilder = ExactPath("/debug/all")) { mockMvcData ->
            mockMvcData.statusIsOk()
            mockMvcData.andExpect(content().string("bacon"))
        }

    }
}