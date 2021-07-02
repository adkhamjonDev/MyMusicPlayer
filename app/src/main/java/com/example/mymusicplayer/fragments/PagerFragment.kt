package com.example.mymusicplayer.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymusicplayer.databinding.FragmentPagerBinding
import com.example.mymusicplayer.models.PageData
private const val ARG_PARAM1 = "param1"
class PagerFragment : Fragment() {
    lateinit var pageData: PageData
    private lateinit var binding: FragmentPagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageData = it.getSerializable(ARG_PARAM1)  as PageData
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentPagerBinding.inflate(inflater, container, false)
        binding.pageData=pageData
        binding.imageView.setImageResource(pageData.image!!)
        return binding.root
    }
    companion object {
        @JvmStatic
        fun newInstance(pageData: PageData) =
            PagerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, pageData)
                }
            }
    }
}