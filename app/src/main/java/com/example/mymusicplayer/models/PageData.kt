package com.example.mymusicplayer.models

import java.io.Serializable
class PageData:Serializable {
    var text:String?=null
    var description:String?=null
    var image:Int?=null
    constructor(image: Int?,text: String?, description: String?) {
        this.image = image
        this.text = text
        this.description = description
    }
    constructor()
}