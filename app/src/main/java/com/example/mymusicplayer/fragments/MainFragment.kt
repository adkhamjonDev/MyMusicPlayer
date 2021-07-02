package com.example.mymusicplayer.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.mymusicplayer.R
import com.example.mymusicplayer.adapters.MyPagerAdapter
import com.example.mymusicplayer.databinding.FragmentMainBinding
import com.example.mymusicplayer.db.AppDataBase
import com.example.mymusicplayer.models.MusicClass
import com.example.mymusicplayer.models.MusicClassRoom
import com.example.mymusicplayer.models.PageData
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var infoList:ArrayList<PageData>
    private lateinit var myPagerAdapter: MyPagerAdapter
    lateinit var appDataBase: AppDataBase
    lateinit var list:ArrayList<MusicClass>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentMainBinding.inflate(inflater, container, false)
        loadData()
        appDataBase= AppDataBase.getInstance(requireContext())
        //---------------------------------------------------------
        Dexter.withContext(context)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                @RequiresApi(Build.VERSION_CODES.R)
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    list= ArrayList(getMusicList())
                    val listRoom=ArrayList<MusicClassRoom>()
                    list.forEach {music->
                        listRoom.add(
                            MusicClassRoom(tittle = music.tittle, artist = music.artist,
                            uri = music.uri, album = music.album)
                        )
                    }
                    appDataBase.musicDao().addAllMusic(listRoom)
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
        //----------------------------------------------------------
        myPagerAdapter= MyPagerAdapter(infoList,childFragmentManager)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                when (position) {
                    0 -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            activity!!.window.statusBarColor = activity!!.getColor(R.color.back_1)
                        }
                        binding.cardView.setCardBackgroundColor(Color.parseColor("#5E4E12"))
                    }
                    1 -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            activity!!.window.statusBarColor = activity!!.getColor(R.color.back_2)
                        }
                        binding.cardView.setCardBackgroundColor(Color.parseColor("#6F6331"))
                    }
                    2 -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            activity!!.window.statusBarColor = activity!!.getColor(R.color.back_3)
                        }
                        binding.cardView.setCardBackgroundColor(Color.parseColor("#6A5400"))
                    }
                    3 -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            activity!!.window.statusBarColor = activity!!.getColor(R.color.back_4)
                        }
                        binding.cardView.setCardBackgroundColor(Color.parseColor("#6F6426"))
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                binding.indicatorView.selection=position
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding.viewPager.adapter=myPagerAdapter
        binding.cardView.setOnClickListener{
            findNavController().popBackStack()
            findNavController().navigate(R.id.musicListFragment)

            val navGraph = findNavController().graph
            navGraph.startDestination = R.id.musicListFragment
            findNavController().graph = navGraph
        }
        return binding.root
    }
    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.R)
    fun getMusicList():List<MusicClass> {
        val listMusic = ArrayList<MusicClass>()
        val contentResolver: ContentResolver? = activity?.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor? = contentResolver?.query(uri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val title: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                val modelAudio =
                    MusicClass(tittle = title, artist = artist, uri = url, album = album)
                listMusic.add(modelAudio)
            } while (cursor.moveToNext())
        }
        return listMusic
    }
    private fun loadData() {
        infoList = ArrayList()
        infoList.add(
            PageData(
                R.drawable.bit1,
                "Welcome",
                "My idea is that there is music in the air, music all around us; the world is full of it, and you simply take as much as you require."
            )
        )
        infoList.add(
            PageData(
                R.drawable.bit2,
                "Welcome",
                "My idea is that there is music in the air, music all around us; the world is full of it, and you simply take as much as you require."
            )
        )
        infoList.add(
            PageData(
                R.drawable.bit3,
                "Welcome",
                "My idea is that there is music in the air, music all around us; the world is full of it, and you simply take as much as you require."
            )
        )
        infoList.add(
            PageData(
                R.drawable.bit4,
                "Welcome",
                "My idea is that there is music in the air, music all around us; the world is full of it, and you simply take as much as you require."
            )
        )
    }
}