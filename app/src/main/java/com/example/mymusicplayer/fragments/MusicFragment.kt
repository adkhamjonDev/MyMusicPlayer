package com.example.mymusicplayer.fragments
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.view.*
import android.widget.PopupMenu
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mymusicplayer.R
import com.example.mymusicplayer.adapters.RvAdapterList2
import com.example.mymusicplayer.databinding.FragmentMusicBinding
import com.example.mymusicplayer.db.AppDataBase
import com.example.mymusicplayer.models.MusicClassRoom


class MusicFragment : Fragment(),PopupMenu.OnMenuItemClickListener {
    private lateinit var binding:FragmentMusicBinding
    private lateinit var list:ArrayList<MusicClassRoom>
    private lateinit var appDataBase: AppDataBase
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var rvAdapterList2: RvAdapterList2
    lateinit var handler: Handler
    private var currentPos=0
    private var totalDuration=0
    private var position=0
    private var lastPosition=0
    private var count=0
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentMusicBinding.inflate(inflater, container, false)
        handler = Handler(Looper.getMainLooper())
        mediaPlayer= MediaPlayer()
        appDataBase= AppDataBase.getInstance(requireContext())
        list= ArrayList(appDataBase.musicDao().getMusics())
        if(arguments!=null){
            position=arguments?.get("position")as Int
        }
        play(position)
        binding.totalNumber.text="/${list.size}"
        //Seek bar
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        //------------------------------------------------
        binding.pause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                binding.pause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }else{
                mediaPlayer.start()
                binding.pause.setImageResource(R.drawable.ic_baseline_pause_24)
            }
        }
        binding.forMus.setOnClickListener {
            if (position < list.size - 1) {
                position++
                play(position)
            } else {
                position = 0
                play(position)
            }
        }
        binding.prevMus.setOnClickListener {
            if (position>0) {
                position--
                play(position)
            } else {
                position =list.size-1
                play(position)
            }
        }
        binding.forw.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition.plus(30000))
        }
        binding.prev.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition.minus(30000))
        }
        mediaPlayer.setOnCompletionListener {
            play(++position)
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        //----------------------------------------------------
        binding.playlist.setOnClickListener {
            binding.playlist.visibility=View.GONE
            binding.like.visibility=View.GONE
            binding.back.visibility=View.GONE
            binding.share.visibility=View.GONE
            binding.volume.visibility=View.GONE
            binding.more.visibility=View.GONE
            binding.mainCard.visibility=View.GONE
            binding.name.visibility=View.GONE
            binding.author.visibility=View.GONE
            //--------------------------------------------
            binding.back2.visibility=View.VISIBLE
            binding.recView.visibility=View.VISIBLE
            binding.totalNumber.visibility=View.VISIBLE
            binding.currentNumber.visibility=View.VISIBLE
            rvAdapterList2=RvAdapterList2(position,list,object:RvAdapterList2.OnItemClickListener{
                override fun onItemMusic(pos: Int, name: TextView, author: TextView) {
                    count++
                    lastPosition=pos
                    mediaPlayer.pause()
                    play(pos)
                    binding.music=list[pos]
                    setDetails(pos,name, author)

                }
            })
            binding.recView.scrollToPosition(position)
            binding.recView.adapter=rvAdapterList2
        }
        binding.volume.setOnClickListener {
            val audio = activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_SAME,AudioManager.FLAG_SHOW_UI)

        }
        binding.share.setOnClickListener {
            try{
                val  shareIntent =  Intent(Intent.ACTION_SEND);
                shareIntent.type = "audio/mp3"
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(list[position].uri).toString())
                startActivity(Intent.createChooser(shareIntent, "sharing mp3 file"));
            }catch (e:Exception){
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
        binding.back2.setOnClickListener {
            binding.back2.visibility=View.GONE
            binding.recView.visibility=View.GONE
            binding.totalNumber.visibility=View.GONE
            binding.currentNumber.visibility=View.GONE

            binding.mainCard.visibility=View.VISIBLE
            binding.name.visibility=View.VISIBLE
            binding.author.visibility=View.VISIBLE
            binding.playlist.visibility=View.VISIBLE
            binding.like.visibility=View.VISIBLE
            binding.back.visibility=View.VISIBLE
            binding.share.visibility=View.VISIBLE
            binding.volume.visibility=View.VISIBLE
            binding.more.visibility=View.VISIBLE
        }
        binding.more.setOnClickListener {
            val popup = PopupMenu(requireContext(),it)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.menu_popup)
            popup.show()
        }
        binding.like.setOnClickListener {
            binding.like.visibility=View.INVISIBLE
            binding.unlike.visibility=View.VISIBLE
            binding.unlike.setImageResource(R.drawable.ic_heart__2_)
        }
        binding.unlike.setOnClickListener {
            binding.like.visibility=View.VISIBLE
            binding.unlike.visibility=View.INVISIBLE
            binding.unlike.setImageResource(R.drawable.ic_heart)
        }
        return binding.root
    }
    fun play(pos: Int){
        mediaPlayer.reset()
        mediaPlayer.setDataSource(list[pos].uri)
        mediaPlayer.prepare()
        mediaPlayer.start()
        currentPos = mediaPlayer.currentPosition
        totalDuration = mediaPlayer.duration
        binding.seekBar.max = totalDuration
        binding.total.text = timerConversion(totalDuration.toLong())
        binding.current.text = timerConversion(currentPos.toLong())
        handler.postDelayed(runnable, 100)
        binding.music=list[position]
        setDetails2(position)
    }
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            binding.seekBar.progress =mediaPlayer.currentPosition
            binding.current.text=(timerConversion(mediaPlayer.currentPosition.toLong()))
            handler.postDelayed(this, 500)
        }
    }
    fun timerConversion(value: Long): String {
        val audioTime: String
        val dur = value.toInt()
        val hrs = dur / 3600000
        val mns = dur / 60000 % 60000
        val scs = dur % 60000 / 1000
        audioTime = if (hrs > 0) {
            String.format("%02d:%02d:%02d", hrs, mns, scs)
        } else {
            String.format("%02d:%02d", mns, scs)
        }
        return audioTime
    }
    @SuppressLint("SetTextI18n")
    fun setDetails(pos:Int, name: TextView, author: TextView){
        binding.music=list[pos]
        binding.currentNumber.text="${pos+1}"
        val thumb = ThumbnailUtils.createVideoThumbnail(list[pos].uri, MediaStore.Images.Thumbnails.MINI_KIND)
        binding.imageIcon.setImageBitmap(thumb)
        name.setTextColor(Color.BLUE)
        author.setTextColor(Color.BLUE)
    }
    fun setDetails2(pos:Int){
        binding.music=list[pos]
        binding.currentNumber.text="${pos+1}"
        val thumb = ThumbnailUtils.createVideoThumbnail(list[pos].uri, MediaStore.Images.Thumbnails.MINI_KIND)
        binding.imageIcon.setImageBitmap(thumb)
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.pause()
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.settings->{
                val soundIntent = Intent(Settings.ACTION_SOUND_SETTINGS)
                startActivity(soundIntent)
            }else->{
                return false
            }
        }
        return false
    }
}