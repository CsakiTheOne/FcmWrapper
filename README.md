# FcmWrapper - A Simple Firebase Cloud Messaging Wrapper for Android

[![](https://jitpack.io/v/CsakiTheOne/FcmWrapper.svg)](https://jitpack.io/#CsakiTheOne/FcmWrapper)

FcmWrapper is a simple Android library that provides an easy way to send messages to your users via Firebase Cloud Messaging (FCM) on Android.

## Usage

### Add dependency to your project

In the `settings.gradle.kts` file:

```kt
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' } // ADD THIS LINE
    }
}
```

In module level `build.gradle.kts` file:

```kt
dependencies {
    implementation("com.github.CsakiTheOne:FcmWrapper:<VERSION>")
}
```

### Obtain access token

```kt
val json = /* GOOGLE SERVICE ACCOUNT KEY FILE */
val accessToken = FcmAccessToken.get(json)

//TODO: use the access token in the header of an http request
```

### Create a message

```kt
val message = FcmMessage(
    notification = NotificationConfig(title = "Hello World!"),
).setTarget(FcmTargetType.TOKEN, "device_token")

//TODO: use the message as a body in an http request
```

## OkHttp compatibility

```kt
val accessToken = FcmAccessToken.get(json)
val message = FcmMessage()

val okHttpRequest = Request.Builder()
    .url("https://fcm.googleapis.com/v1/projects/<project_id>/messages:send")
    .addFcmAccessToken(accessToken)
    .post(message.toRequestBody())
    .build()
OkHttpClient().newCall(request).execute()
```
