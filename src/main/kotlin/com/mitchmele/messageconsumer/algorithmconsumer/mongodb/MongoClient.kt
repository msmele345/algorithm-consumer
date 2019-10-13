package com.mitchmele.messageconsumer.algorithmconsumer.mongodb

import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.AlgorithmClient
import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.AlgorithmDomainModel
import com.mitchmele.messageconsumer.algorithmconsumer.result.*
import org.springframework.stereotype.Service


@Service
class MongoClient(
    private val mongoRepo: AlgorithmMongoRepository
) : AlgorithmClient {

    override fun saveAlgorithm(algorithm: AlgorithmDomainModel): Result<Unit, ServiceErrors> {
        return try {
            mongoRepo.save(algorithm)
            Success(Unit)
        } catch (e: Exception) {
            Failure(serviceErrorOf(ServiceError(
                errorType = ErrorType.INPUT_VALIDATION,
                errorMessage = e.localizedMessage,
                service = ServiceName.MONGO
            )))
        }
    }

    override fun findAlgorithmByName(algorithmName: String): Result<AlgorithmDomainModel, ServiceErrors> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAlgorithmByName(algorithmName: String): Result<Unit, ServiceErrors> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}