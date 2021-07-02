package com.example.mymusicplayer.models
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
data class MusicClass(
    var tittle:String,
    var artist:String,
    var uri: String,
    var album: String
)