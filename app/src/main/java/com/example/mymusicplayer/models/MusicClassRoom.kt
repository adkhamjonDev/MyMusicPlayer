package com.example.mymusicplayer.models
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "music")
data class MusicClassRoom(
    var tittle:String,
    var artist:String,
    @PrimaryKey
    var uri: String,
    var album: String
)