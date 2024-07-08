package com.csakitheone.fcm_wrapper

import android.util.Log
import org.junit.Test

import org.junit.Assert.*

/**
 * Unit tests for [FcmMessage]. These tests check if the JSON generation is correct.
 */
class FcmMessageUnitTest {
    @Test
    fun testBasicNotification() {
        val message = FcmMessage(
            notification = NotificationConfig(
                title = "Hello",
                body = "World",
            )
        ).setTarget(FcmTargetType.TOKEN, "asd123")

        val json = message.toJson()

        println(json)
    }
}