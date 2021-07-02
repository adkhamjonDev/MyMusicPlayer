package com.example.mymusicplayer.adapters
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicplayer.databinding.MyItemBinding
import com.example.mymusicplayer.models.MusicClassRoom
class RvAdapterList( var list: List<MusicClassRoom>, var onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<RvAdapterList.MyViewHolder>(){
    inner class MyViewHolder(var myItemBinding: MyItemBinding): RecyclerView.ViewHolder(
        myItemBinding.root){
        fun onBind(musicClass: MusicClassRoom){
            myItemBinding.music=musicClass
            val thumb = ThumbnailUtils.createVideoThumbnail(musicClass.uri, MediaStore.Images.Thumbnails.MINI_KIND)
            myItemBinding.imageIcon.setImageBitmap(thumb)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            MyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemMusic(position)
        }

    }
    override fun getItemCount(): Int {
        return list.size
    }
    interface OnItemClickListener {
        fun onItemMusic(position:Int)
    }
}