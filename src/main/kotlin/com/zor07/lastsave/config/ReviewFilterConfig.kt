package com.zor07.lastsave.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReviewFilterConfig {

    @Bean
    fun reviewProtectionFilter(
        @Value("\${review.secret-token}") secretToken: String,
    ): FilterRegistrationBean<ReviewProtectionFilter> {
        val registration = FilterRegistrationBean(ReviewProtectionFilter(secretToken))
        registration.addUrlPatterns("/api/pr-review/*", "/api/pr-review")
        return registration
    }
}

class ReviewProtectionFilter(
    private val secretToken: String,
) : Filter {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token = (request as HttpServletRequest).getHeader("X-Secret-Token")
        if (token != secretToken) {
            logger.warn("Rejected request to {} due to invalid token", request.requestURI)
            (response as HttpServletResponse).status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }
        chain.doFilter(request, response)
    }
}
