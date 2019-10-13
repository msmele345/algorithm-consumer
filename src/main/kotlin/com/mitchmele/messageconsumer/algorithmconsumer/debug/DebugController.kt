package com.mitchmele.messageconsumer.algorithmconsumer.debug

import com.mitchmele.messageconsumer.algorithmconsumer.algorithm_store.AlgorithmDomainModel
import com.mitchmele.messageconsumer.algorithmconsumer.mongodb.AlgorithmMongoRepository
import org.litote.kmongo.KMongo
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@ContextConfiguration
class DebugController(
    private val mongoRepository: AlgorithmMongoRepository
) {

    @RequestMapping("/debug/all")
    fun showAllAlgorithms() : ResponseEntity<*> {
        val client = KMongo.createClient()
        val database = client.getDatabase("algorithmDomainModels")
        val data = database.getCollection("algorithmDomainModels")

        val count = data.countDocuments().toString()
        return ResponseEntity.ok().body("Count: $count")
    }

}