package com.zor07.lastsave.dto.github

import com.fasterxml.jackson.annotation.JsonProperty

data class RepoPublicKeyResponse(
    @JsonProperty("key_id") val keyId: String,
    val key: String,
)
