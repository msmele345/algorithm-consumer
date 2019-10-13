package com.mitchmele.messageconsumer.algorithmconsumer.mongodb

import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.AlgorithmDomainModel
import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.Tag
import com.mitchmele.messageconsumer.algorithmconsumer.result.*
import com.mitchmele.messageconsumer.algorithmconsumer.utils.UnitTest
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.experimental.categories.Category
import java.lang.RuntimeException

@Category(UnitTest::class)
class MongoClientTest {

    val mockMongoRepo: AlgorithmMongoRepository = mock()

    val subject = MongoClient(mockMongoRepo)

    val inputAMD = AlgorithmDomainModel(
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



    @Test
    fun `addAlgorithm - should call the mongo repository`() {
        val expected = Success(inputAMD)

        whenever(mockMongoRepo.save(inputAMD)) doReturn inputAMD

        subject.saveAlgorithm(inputAMD)

        verify(mockMongoRepo,times(1)).findById(any())

    }



    @Test
    fun `addAlgorithm - should return a success of Unit if the ADM is saved successfully`() {
        val expected = Success(Unit)

        whenever(mockMongoRepo.save(inputAMD)) doReturn inputAMD

        val actual = subject.saveAlgorithm(inputAMD)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `addAlgorithm - should return ServiceErrors if the repo throws an exception`() {
        val expected = Failure(serviceErrorOf(ServiceError(
            errorType = ErrorType.INPUT_VALIDATION,
            errorMessage = "something happened during save",
            service = ServiceName.MONGO

        )))

        whenever(mockMongoRepo.save(inputAMD)) doThrow RuntimeException("something happened during save")

        val actual = subject.saveAlgorithm(inputAMD)

        assertThat(actual).isEqualTo(expected)
    }

}