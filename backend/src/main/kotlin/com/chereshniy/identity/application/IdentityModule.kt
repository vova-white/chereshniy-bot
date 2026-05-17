package com.chereshniy.identity.application

import io.ktor.server.config.ApplicationConfig
import kotlinx.serialization.Serializable

class IdentityModule private constructor(
    private val twitchOAuthGateway: TwitchOAuthGateway,
    private val oauthSettings: TwitchOAuthSettings,
    private val streamerDirectory: StreamerDirectory,
) {
    fun twitchAuthorizationUrl(state: String): String = twitchOAuthGateway.authorizationUrl(oauthSettings, state)

    suspend fun signInWithTwitch(code: String): ApplicationSession {
        val twitchSignIn = twitchOAuthGateway.exchangeCodeForStreamer(oauthSettings, code)
        val streamer = streamerDirectory.recordTwitchSignIn(twitchSignIn)
        streamerDirectory.ensureCurrentAuctionList(streamer.id)
        return ApplicationSession(streamer.id)
    }

    fun currentSession(session: ApplicationSession): AdminSessionView? {
        val streamer = streamerDirectory.findStreamer(session.streamerId) ?: return null
        val auctionList = streamerDirectory.findCurrentAuctionList(session.streamerId) ?: return null

        return AdminSessionView(
            streamer = StreamerSessionView(
                displayName = streamer.displayName,
                publicAuctionSlug = streamer.publicAuctionSlug,
            ),
            pointAuction = PointAuctionSummaryView(
                auctionTitle = auctionList.title,
                statusLabel = if (auctionList.active) "Open Bidding Status" else "Closed Bidding Status",
                lotCount = auctionList.lotCount,
                pendingBidCount = 0,
            ),
        )
    }

    companion object {
        fun inMemory(
            twitchOAuthGateway: TwitchOAuthGateway,
            oauthSettings: TwitchOAuthSettings,
        ): IdentityModule = IdentityModule(
            twitchOAuthGateway = twitchOAuthGateway,
            oauthSettings = oauthSettings,
            streamerDirectory = InMemoryStreamerDirectory(),
        )

        fun fromEnvironment(config: ApplicationConfig): IdentityModule = inMemory(
            twitchOAuthGateway = DefaultTwitchOAuthGateway(),
            oauthSettings = TwitchOAuthSettings.fromEnvironment(config),
        )
    }
}

@Serializable
data class AdminSessionView(
    val streamer: StreamerSessionView,
    val pointAuction: PointAuctionSummaryView,
)

@Serializable
data class StreamerSessionView(
    val displayName: String,
    val publicAuctionSlug: String,
)

@Serializable
data class PointAuctionSummaryView(
    val auctionTitle: String,
    val statusLabel: String,
    val lotCount: Int,
    val pendingBidCount: Int,
)

private interface StreamerDirectory {
    fun recordTwitchSignIn(twitchSignIn: TwitchSignInResult): StreamerRecord

    fun ensureCurrentAuctionList(streamerId: String): AuctionListRecord

    fun findStreamer(streamerId: String): StreamerRecord?

    fun findCurrentAuctionList(streamerId: String): AuctionListRecord?
}

private data class StreamerRecord(
    val id: String,
    val twitchUserId: String,
    val displayName: String,
    val publicAuctionSlug: String,
)

private data class AuctionListRecord(
    val streamerId: String,
    val title: String = "Point Auction",
    val active: Boolean = false,
    val lotCount: Int = 0,
)

private data class TwitchTokenCustodyRecord(
    val streamerId: String,
    val accessToken: String,
    val refreshToken: String,
)

private class InMemoryStreamerDirectory : StreamerDirectory {
    private val streamersById = linkedMapOf<String, StreamerRecord>()
    private val streamerIdsByTwitchUserId = linkedMapOf<String, String>()
    private val streamerIdsByPublicSlug = linkedMapOf<String, String>()
    private val auctionListsByStreamerId = linkedMapOf<String, AuctionListRecord>()
    private val tokenCustodyByStreamerId = linkedMapOf<String, TwitchTokenCustodyRecord>()

    override fun recordTwitchSignIn(twitchSignIn: TwitchSignInResult): StreamerRecord {
        val streamerId = streamerIdsByTwitchUserId.getOrPut(twitchSignIn.twitchUserId) {
            "streamer-${twitchSignIn.twitchUserId}"
        }
        val publicSlug = twitchSignIn.twitchLogin.publicAuctionSlug()
        val conflictingStreamerId = streamerIdsByPublicSlug[publicSlug]
        check(conflictingStreamerId == null || conflictingStreamerId == streamerId) {
            "Public Auction Slug is already owned by another Streamer."
        }

        streamersById[streamerId]?.publicAuctionSlug?.let { oldSlug ->
            if (oldSlug != publicSlug) {
                streamerIdsByPublicSlug.remove(oldSlug)
            }
        }

        val streamer = StreamerRecord(
            id = streamerId,
            twitchUserId = twitchSignIn.twitchUserId,
            displayName = twitchSignIn.displayName,
            publicAuctionSlug = publicSlug,
        )
        streamersById[streamerId] = streamer
        streamerIdsByPublicSlug[publicSlug] = streamerId
        tokenCustodyByStreamerId[streamerId] = TwitchTokenCustodyRecord(
            streamerId = streamerId,
            accessToken = twitchSignIn.accessToken,
            refreshToken = twitchSignIn.refreshToken,
        )
        return streamer
    }

    override fun ensureCurrentAuctionList(streamerId: String): AuctionListRecord =
        auctionListsByStreamerId.getOrPut(streamerId) {
            AuctionListRecord(streamerId = streamerId)
        }

    override fun findStreamer(streamerId: String): StreamerRecord? = streamersById[streamerId]

    override fun findCurrentAuctionList(streamerId: String): AuctionListRecord? = auctionListsByStreamerId[streamerId]
}

private fun String.publicAuctionSlug(): String {
    val slug = trim().lowercase().replace(Regex("[^a-z0-9_]+"), "-").trim('-')
    require(slug.isNotBlank()) { "Twitch login cannot produce a blank Public Auction Slug." }
    return slug
}
