package com.csakitheone.fcm_wrapper

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

/**
 * A Firebase Cloud Messaging access token. You can get an access token with the [get] method.
 * These access tokens expire after a short time, so you should get a new one every time you send a message.
 * You can add the access token to an OkHttp request with the [addFcmAccessToken] method,
 * but if you use another HTTP client, you can add the access token to the headers manually.
 * ```
 * {
 *   "Authorization": "Bearer <access_token>",
 *   "Content-Type": "application/json"
 *   other headers...
 * }
 * ```
 */
class FcmAccessToken private constructor(val accessToken: String) {
    companion object {

        /**
         * Get an access token for Firebase Cloud Messaging.
         * > This method contains a network operation and mustn't be called on the main thread.
         * @param googleServiceAccountJsonString You need a private key file for a service account to get an access token. You can generate a private key file in the Firebase console.
         * @return An access token for Firebase Cloud Messaging or null if an error occurred.
         */
        fun get(
            googleServiceAccountJsonString: String,
        ): FcmAccessToken? {
            try {
                val stream: InputStream =
                    ByteArrayInputStream(googleServiceAccountJsonString.toByteArray(StandardCharsets.UTF_8))
                val googleCredentials =
                    GoogleCredentials.fromStream(stream)
                        .createScoped("https://www.googleapis.com/auth/firebase.messaging")
                googleCredentials.refresh()
                return FcmAccessToken(googleCredentials.accessToken.tokenValue)
            } catch (e: Exception) {
                Log.e("FcmAccessToken", e.message ?: "Unknown error. Make sure that you call this method on the correct thread!")
                return null
            }
        }

        /**
         * Add the Firebase Cloud Messaging access token to an OkHttp request's headers.
         * @param accessToken The Firebase Cloud Messaging access token.
         * @return The OkHttp request builder with the access token added to the headers.
         */
        fun okhttp3.Request.Builder.addFcmAccessToken(accessToken: FcmAccessToken): okhttp3.Request.Builder {
            return this.addHeader("Authorization", "Bearer ${accessToken.accessToken}")
        }

    }
}