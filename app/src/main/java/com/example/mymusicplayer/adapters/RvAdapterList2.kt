package com.example.mymusicplayer.adapters
import android.graphics.Color
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicplayer.databinding.MyItem2Binding
import com.example.mymusicplayer.models.MusicClassRoom
class RvAdapterList2(var positionItem: Int,var list: List<MusicClassRoom>, var onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<RvAdapterList2.MyViewHolder>(){
    inner class MyViewHolder(var myItem2Binding: MyItem2Binding): RecyclerView.ViewHolder(
        myItem2Binding.root){
        fun onBind(musicClass: MusicClassRoom){
            myItem2Binding.music=musicClass
            val thumb = ThumbnailUtils.createVideoThumbnail(musicClass.uri, MediaStore.Images.Thumbnails.MINI_KIND)
            myItem2Binding.imageIcon.setImageBitmap(thumb)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            MyItem2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position])
        if(positionItem==position){
            holder.myItem2Binding.tittle.setTextColor(Color.BLUE)
            holder.myItem2Binding.description.setTextColor(Color.BLUE)
        }
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemMusic(position,holder.myItem2Binding.tittle,holder.myItem2Binding.description)
        }

    }
    override fun getItemCount(): Int {
        return list.size
    }
    interface OnItemClickListener {
        fun onItemMusic(pos:Int,name:TextView,author:TextView)
    }
}