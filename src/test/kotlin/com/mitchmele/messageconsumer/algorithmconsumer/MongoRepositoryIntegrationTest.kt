package com.mitchmele.messageconsumer.algorithmconsumer

import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.AlgorithmDomainModel
import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Tag
import com.mitchmele.messageconsumer.algorithmconsumer.mongodb.AlgorithmMongoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [MongoRepositoryIntegrationTest.TestConfig::class])
class MongoRepositoryIntegrationTest {


    @Autowired
    private lateinit var mongoRepo: AlgorithmMongoRepository


    @Before
    fun setUp() {
        mongoRepo.run {
//            deleteAll()
            saveAll(listOf(AlgorithmDomainModel(
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
                )))
        }
    }

//    @After
//    fun tearDown() {
//        mongoRepo.run { deleteAll() }
//    }

    @Test
    fun `test connection`() {

    }


    @Test
    fun `findAll - should find all domain objects and return a list of objects upon success`() {

        val expected = listOf(AlgorithmDomainModel(
            name = "capitalizeString",
            codeSnippet = """
                fun capitalizeString(str: String): String = str.toUpperCase() 
            """.trimIndent(),
            category = com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Category(
                categoryDescription = "EASY",
                difficultlyLevel = 2,
                tags = listOf(Tag("string manipulation"))
            )
        ), AlgorithmDomainModel(
            name = "sortNumberArray",
            codeSnippet = """
                fun sortArray(array: List<Int>): List<Int> = array.sort() 
            """.trimIndent(),
            category = com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Category(
                categoryDescription = "EASY",
                difficultlyLevel = 2,
                tags = listOf(Tag("Collections"))
            )
        )
        )

        val actual = mongoRepo.findAll()

        assertThat(actual).isNotNull
        assertThat(actual.size).isEqualTo(2)
        assertThat(actual).isEqualTo(expected)
    }


    @Test
    fun `findById - should return the correct ADM when provided the correct ID`() {

        val expected = AlgorithmDomainModel(
            name = "sortNumberArray",
            codeSnippet = """
                fun sortArray(array: List<Int>): List<Int> = array.sort() 
            """.trimIndent(),
            category = com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Category(
                categoryDescription = "EASY",
                difficultlyLevel = 2,
                tags = listOf(Tag("Collections"))
            )
        )

        val actual = mongoRepo.findById("sortNumberArray")

        assertThat(actual).isPresent
        assertThat(actual).isEqualTo(Optional.of(expected))
    }


    @Configuration
    @EnableMongoRepositories(basePackages = ["com.mitchmele.messageconsumer.*"])
    class TestConfig : AbstractMongoConfiguration() {
        override fun mongoClient(): com.mongodb.MongoClient {
            return com.mongodb.MongoClient("0.0.0.0", 27017)
        }

        override fun getDatabaseName(): String {
            return "algorithmDomainModels"
        }
    }
}