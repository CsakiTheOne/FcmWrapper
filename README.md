# FcmWrapper - A Simple Firebase Cloud Messaging Wrapper for Android

[![](https://jitpack.io/v/CsakiTheOne/FcmWrapper.svg)](https://jitpack.io/#CsakiTheOne/FcmWrapper)

FcmWrapper is a simple Android library that provides an easy way to send messages to your users via
Firebase Cloud Messaging (FCM) on Android.

## Usage

### Add dependency to your project

In the `settings.gradle.kts` file:

```kt
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // ADD THIS LINE
    }
}
```

In module level `build.gradle.kts` file:

```kt
dependencies {
    implementation("com.github.CsakiTheOne:FcmWrapper:<VERSION>")
}
```

> If you get an error similar to this: `2 files found with path 'meta-inf/dependencies'`,
> then modify your module level `build.gradle.kts` like this:

```kt
android {
    ...
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/DEPENDENCIES" // ADD THIS LINE
        }
    }
}
```

### Obtain access token

To get an access token, you need to provide a service account key file in JSON format. You can
generate a service account key in the Firebase Console.

1. In the Firebase Console, open
   Settings > [Service Accounts](https://console.firebase.google.com/project/_/settings/serviceaccounts/adminsdk).
2. Click Generate New Private Key, then confirm by clicking Generate Key.
3. Save the JSON file containing the key.

```kt
val json = /* GOOGLE SERVICE ACCOUNT KEY FILE */
val accessToken = FcmAccessToken.get(json)

//TODO: use the access token in the header of an http request
```

### Create a message

FcmMessage and its subclasses are based
on [this reference](https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages).

```kt
val message = FcmMessage(
    notification = NotificationConfig(title = "Hello World!"),
).setTarget(FcmTargetType.TOKEN, "device_token")

//TODO: use the message as a body in an http request
```

## OkHttp compatibility

The library provides extension functions for OkHttp to make it easier to send messages.

```kt
val accessToken = FcmAccessToken.get(json)
val message = FcmMessage()

val okHttpRequest = Request.Builder()
    .url("https://fcm.googleapis.com/v1/projects/<project_id>/messages:send")
    .addFcmAccessToken(accessToken) // <- extension function
    .post(message.toRequestBody()) // <- FcmMessage object can be converted to RequestBody
    .build()
OkHttpClient().newCall(request).execute()
```
