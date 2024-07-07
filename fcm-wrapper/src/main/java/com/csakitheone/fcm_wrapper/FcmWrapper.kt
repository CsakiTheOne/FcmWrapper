package com.csakitheone.fcm_wrapper

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class FcmWrapper {
    companion object {

        internal val gson = GsonBuilder()
            .create()

    }
}