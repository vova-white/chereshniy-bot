package com.chereshniy.identity.application

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationSession(val streamerId: String)
