package example.omnidian.jbr

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@SpringBootApplication
class Application {
  @Bean("dangerous")
  fun getDangerousObjectMapper(): ObjectMapper =
      ObjectMapper().registerModule(KotlinModule.Builder().build())

  @Primary      // mark this one @Primary to satisfy Spring Boot
  @Bean("safe")
  fun getSafeObjectMapper(): ObjectMapper = ObjectMapper()
}

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
interface ITarget

interface IInjectable

@Service
class DangerousInjectable(dangerous: ObjectMapper) : IInjectable

@Service
class SafeInjectable (safe: ObjectMapper) : IInjectable

data class DangerousTarget(
    val data: String,
    @JacksonInject val injectable: DangerousInjectable,
) : ITarget

// This class needs Jackson annotations as it will be deserialised by an ObjectMapper without the
// Kotlin module installed
data class SafeTarget @JsonCreator constructor(
    @JsonProperty("data") val data: String,
    @JacksonInject val injectable: SafeInjectable,
) : ITarget

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}