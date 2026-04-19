package com.zor07.lastsave.dto.review

import com.fasterxml.jackson.annotation.JsonProperty

data class PrReportRequest(
    @JsonProperty("github_username") val githubUsername: String,
    @JsonProperty("pr_url") val prUrl: String,
    val passed: Boolean,
    @JsonProperty("failed_tests") val failedTests: List<String>,
    val diff: String,
)
