package com.example.mymusicplayer.fragments
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.mymusicplayer.R
import com.example.mymusicplayer.adapters.RvAdapterList
import com.example.mymusicplayer.databinding.FragmentMusicListBinding
import com.example.mymusicplayer.db.AppDataBase
import com.example.mymusicplayer.models.MusicClassRoom
class MusicListFragment : Fragment() {
    private lateinit var binding: FragmentMusicListBinding
    private lateinit var list: ArrayList<MusicClassRoom>
    private lateinit var rvAdapterList: RvAdapterList
    private lateinit var appDataBase: AppDataBase
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicListBinding.inflate(inflater, container, false)
        appDataBase= AppDataBase.getInstance(requireContext())
        list= ArrayList(appDataBase.musicDao().getMusics())
        binding.total.text="${list.size} tracks"
        rvAdapterList= RvAdapterList(list,object:RvAdapterList.OnItemClickListener{
            override fun onItemMusic(position: Int) {
                val bundle= bundleOf("position" to position)
                findNavController().navigate(R.id.musicFragment,bundle)
            }
        })
        binding.recView.adapter=rvAdapterList
        binding.exit.setOnClickListener {
            activity?.finish()
        }
        return binding.root
    }
}