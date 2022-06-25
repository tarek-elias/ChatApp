package com.example.chatapp

class User {
    var username: String? = null
    var email: String? = null
    var uid: String? = null
    var imgUrl: String? = null
    constructor(){}

    constructor(username: String?, email: String?, uid: String?, imgUrl: String?)
    {
        this.username = username
        this.email = email
        this.uid = uid
        this.imgUrl = imgUrl
    }


}