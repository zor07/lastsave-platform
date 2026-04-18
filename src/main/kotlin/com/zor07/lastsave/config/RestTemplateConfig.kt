package com.zor07.lastsave.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {

    @Bean
    fun gitHubRestTemplate(builder: RestTemplateBuilder, loggingInterceptor: OutgoingRequestLoggingInterceptor): RestTemplate =
        builder.interceptors(loggingInterceptor).build()

    @Bean
    fun openAiRestTemplate(builder: RestTemplateBuilder, objectMapper: ObjectMapper, loggingInterceptor: OutgoingRequestLoggingInterceptor): RestTemplate =
        builder.messageConverters(MappingJackson2HttpMessageConverter(objectMapper)).interceptors(loggingInterceptor).build()
}
