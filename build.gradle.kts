import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.1.8.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("jvm") version "1.2.71"
	kotlin("plugin.spring") version "1.2.71"
}

group = "com.mitchmele.messageconsumer"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	compile("org.springframework.boot:spring-boot-starter-data-mongodb")
	compile("org.springframework.integration:spring-integration-kafka:3.1.5.RELEASE")
	compile("org.springframework.boot:spring-boot-starter-amqp")
	compile("org.springframework.boot:spring-boot-starter-integration")
	compile("org.springframework.boot:spring-boot-starter-web")
	compile("com.fasterxml.jackson.module:jackson-module-kotlin")
	compile("org.jetbrains.kotlin:kotlin-reflect")
	compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	compile("org.springframework.kafka:spring-kafka")
	compile ("org.jetbrains.kotlin:kotlin-test")
	compile ("org.litote.kmongo:kmongo:3.11.0")
	compile ("no.skatteetaten.aurora.mockmvc.extensions:mockmvc-extensions-kotlin:0.2.2")
	testCompile("com.nhaarman:mockito-kotlin:1.5.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("org.springframework.integration:spring-integration-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
