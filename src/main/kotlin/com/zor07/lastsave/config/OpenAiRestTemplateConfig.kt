package com.zor07.lastsave.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class OpenAiRestTemplateConfig {

    @Bean
    fun openAiRestTemplate(builder: RestTemplateBuilder, objectMapper: ObjectMapper): RestTemplate {
        val converter = MappingJackson2HttpMessageConverter(objectMapper)
        return builder.messageConverters(converter).build()
    }
}
