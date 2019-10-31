package com.athenaworks.mycontacts.Network;

import com.athenaworks.mycontacts.Models.Contacts

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Query


interface Service {

    @GET(" ")
    fun getContacts(@Query("page") page: Int): Call<Contacts>

}