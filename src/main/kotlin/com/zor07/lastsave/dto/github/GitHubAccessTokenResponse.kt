package com.zor07.lastsave.dto.github

import com.fasterxml.jackson.annotation.JsonProperty

data class GitHubAccessTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String?,
)
