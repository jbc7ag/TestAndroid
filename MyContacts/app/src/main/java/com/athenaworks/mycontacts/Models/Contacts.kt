package com.athenaworks.mycontacts.Models;

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import com.athenaworks.mycontacts.Models.Name;
import com.athenaworks.mycontacts.Models.Location;
import com.athenaworks.mycontacts.Models.Login;
import com.athenaworks.mycontacts.Models.Picture;

class Contacts {

    @SerializedName("results")
    @Expose
    var results: ArrayList<Results>? = null

}


class Results {

    var name: Name? = null
    var location: Location? = null
    var login: Login? = null
    var picture: Picture? = null
    var email: String? = null
    var cell: String? = null

}

