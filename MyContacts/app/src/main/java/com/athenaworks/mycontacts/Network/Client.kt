package com.athenaworks.mycontacts.Network;

import android.content.Context
import com.athenaworks.mycontacts.Utils.Utils.Companion.isConnected
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File



class Client {

    companion object {

        fun getClient(): Retrofit {

            val BASE_URL = "https://randomuser.me/api/?results=10&seed=abc"

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit;
        }
    }
}