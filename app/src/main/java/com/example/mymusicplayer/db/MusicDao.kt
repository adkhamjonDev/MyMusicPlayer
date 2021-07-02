package com.example.mymusicplayer.db
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymusicplayer.models.MusicClassRoom
@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllMusic(musicClass: List<MusicClassRoom?>?)
    @Insert
    fun addMusic(musicClass: MusicClassRoom)
    @Query("DELETE FROM music")
    fun clearMusic()
    @Query("select * from music")
    fun getMusics(): List<MusicClassRoom>
}