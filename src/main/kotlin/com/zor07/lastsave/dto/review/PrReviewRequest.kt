package com.zor07.lastsave.dto.review

import com.fasterxml.jackson.annotation.JsonProperty

data class PrReviewRequest(
    @JsonProperty("github_username")
    val githubUsername: String,
    @JsonProperty("pr_url")
    val prUrl: String,
    val diff: String,
)
