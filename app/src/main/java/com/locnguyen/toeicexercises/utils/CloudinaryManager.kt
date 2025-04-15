package com.locnguyen.toeicexercises.utils

import com.cloudinary.Cloudinary

class CloudinaryManager {
    companion object {
        private const val CLOUD_NAME = "toeiclearning"
        private const val API_KEY = "237291759617564"
        private const val API_SECRET = "w3H3_xG5FLMGtuz8apla9eG2PcU"

        fun getConfig(): HashMap<String, String> {
            val config = HashMap<String, String>()
            config["cloud_name"] = CLOUD_NAME
            config["api_key"] = API_KEY
            config["api_secret"] = API_SECRET

            return config
        }
    }
}