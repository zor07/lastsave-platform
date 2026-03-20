package com.zor07.lastsave

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory

class ReviewProtectionFilter(
    private val secretToken: String,
) : Filter {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        if (httpRequest.requestURI.startsWith("/review")) {
            val token = httpRequest.getHeader("X-Secret-Token")
            if (token != secretToken) {
                logger.warn("Rejected request to /review due to invalid token")
                (response as HttpServletResponse).status = HttpServletResponse.SC_UNAUTHORIZED
                return
            }
        }
        chain.doFilter(request, response)
    }
}
