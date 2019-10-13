package com.mitchmele.messageconsumer.algorithmconsumer.mongodb

import com.mongodb.MongoClient
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration
@EnableMongoRepositories(basePackages = ["com.mitchmele.messageconsumer.*"])
class MongoConfig: AbstractMongoConfiguration() {

    override fun mongoClient(): MongoClient {
        return MongoClient("0.0.0.0", 27017)
    }

    override fun getDatabaseName(): String {
        return "algorithmDomainModels" //this has to match the document annotation
    }
}

