package com.csakitheone.fcm_wrapper

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * The type of the target of the message.
 */
enum class FcmTargetType {
    /**
     * The target is a device token. Only one device can be targeted.
     */
    TOKEN,
    /**
     * The target is a topic. All devices subscribed to the topic will receive the message.
     */
    TOPIC,
    /**
     * The target is a condition. The message will be sent to all devices that match the condition.
     * e.g. "'foo' in topics && 'bar' in topics".
     */
    CONDITION,
}

private data class FcmMessageRoot(
    val message: FcmMessage,
)

data class FcmMessage(
    val name: String? = null,
    val data: Map<String, String>? = null,
    val notification: NotificationConfig? = null,
    val android: AndroidConfig? = null,
    val webpush: WebpushConfig? = null,
    val apns: ApnsConfig? = null,
    @SerializedName("fcm_options")
    val fcmOptions: FcmOptions? = null,
    val token: String? = null,
    val topic: String? = null,
    val condition: String? = null,
) {
    private fun validate() {
        if (token == null && topic == null && condition == null) {
            throw IllegalArgumentException("You must set the target of the message with setTarget() before sending it.")
        }

        val targets = listOf(token, topic, condition).filterNotNull()
        if (targets.size > 1) {
            throw IllegalArgumentException("You can only set one target type for the message.")
        }

        if (data == null && notification == null) {
            throw IllegalArgumentException("You must set the data or notification field of the message.")
        }
    }

    /**
     * Set the target of the message. You can set the target to a single device, a topic, or a condition.
     * @param targetType The type of the target.
     * @param target The target. It can be a device token, a topic name, or a condition.
     * @return The FcmMessage with the target set.
     */
    fun setTarget(targetType: FcmTargetType, target: String): FcmMessage {
        return when (targetType) {
            FcmTargetType.TOKEN -> this.copy(token = target, topic = null, condition = null)
            FcmTargetType.TOPIC -> this.copy(topic = target, token = null, condition = null)
            FcmTargetType.CONDITION -> this.copy(condition = target, token = null, topic = null)
        }
    }

    /**
     * Converts the FcmMessage to a JSON string. Use this JSON as the body of the POST request to the FCM API.
     */
    fun toJson(): String {
        validate()

        return FcmWrapper.gson.toJson(FcmMessageRoot(this))
    }

    /**
     * Converts the FcmMessage to an OkHttp RequestBody.
     */
    fun toRequestBody(): RequestBody {
        validate()

        return toJson().toRequestBody("application/json".toMediaType())
    }

    companion object {
        fun fromJson(json: String): FcmMessage {
            return FcmWrapper.gson.fromJson(json, FcmMessageRoot::class.java).message
        }
    }
}

data class NotificationConfig(
    val title: String? = null,
    val body: String? = null,
    val image: String? = null,
)

data class AndroidConfig(
    @SerializedName("collapse_key")
    val collapseKey: String? = null,
    val priority: String? = null,
    val ttl: String? = null,
    @SerializedName("restricted_package_name")
    val restrictedPackageName: String? = null,
    val data: Map<String, String>? = null,
    val notification: AndroidNotificationConfig? = null,
    @SerializedName("fcm_options")
    val fcmOptions: FcmOptions? = null,
    @SerializedName("direct_boot_ok")
    val directBootOk: Boolean? = null,
)

data class AndroidNotificationConfig(
    val title: String? = null,
    val body: String? = null,
    val icon: String? = null,
    val color: String? = null,
    val sound: String? = null,
    val tag: String? = null,
    @SerializedName("click_action")
    val clickAction: String? = null,
    @SerializedName("body_loc_key")
    val bodyLocKey: String? = null,
    @SerializedName("body_loc_args")
    val bodyLocArgs: List<String>? = null,
    @SerializedName("title_loc_key")
    val titleLocKey: String? = null,
    @SerializedName("title_loc_args")
    val titleLocArgs: List<String>? = null,
    @SerializedName("channel_id")
    val channelId: String? = null,
    val ticker: String? = null,
    val sticky: Boolean? = null,
    @SerializedName("event_time")
    val eventTime: String? = null,
    @SerializedName("local_only")
    val localOnly: Boolean? = null,
    @SerializedName("notification_priority")
    val notificationPriority: String? = null,
    @SerializedName("default_sound")
    val defaultSound: Boolean? = null,
    @SerializedName("default_vibrate_timings")
    val defaultVibrateTimings: Boolean? = null,
    @SerializedName("default_light_settings")
    val defaultLightSettings: Boolean? = null,
    @SerializedName("vibrate_timings")
    val vibrateTimings: List<String>? = null,
    val visibility: String? = null,
    @SerializedName("notification_count")
    val notificationCount: Long? = null,
    @SerializedName("light_settings")
    val lightSettings: LightSettings? = null,
    val image: String? = null,
    @SerializedName("bypass_proxy_notification")
    val bypassProxyNotification: Boolean? = null,
    val proxy: String? = null,
)

data class LightSettings(
    val color: Color? = null,
    @SerializedName("light_on_duration")
    val lightOnDuration: String? = null,
    @SerializedName("light_off_duration")
    val lightOffDuration: String? = null,
)

data class Color(
    val red: Long? = null,
    val green: Long? = null,
    val blue: Long? = null,
    val alpha: Long? = null,
)

data class WebpushConfig(
    val headers: Map<String, Any>? = null,
    val data: Map<String, String>? = null,
    val notification: Map<String, Any>? = null,
    @SerializedName("fcm_options")
    val fcmOptions: WebpushFcmOptions? = null,
)

data class WebpushFcmOptions(
    val link: String? = null,
    @SerializedName("analytics_label")
    val analyticsLabel: String? = null,
)

data class ApnsConfig(
    val headers: Map<String, Any>? = null,
    val payload: Map<String, Any>? = null,
    @SerializedName("fcm_options")
    val fcmOptions: ApnsFcmOptions? = null,
)

data class ApnsFcmOptions(
    @SerializedName("analytics_label")
    val analyticsLabel: String? = null,
    val image: String? = null,
)

data class FcmOptions(
    @SerializedName("analytics_label")
    val analyticsLabel: String? = null,
)