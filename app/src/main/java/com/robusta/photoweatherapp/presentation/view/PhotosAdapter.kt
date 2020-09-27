package com.robusta.photoweatherapp.presentation.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robusta.photoweatherapp.databinding.PhotoItemBinding
import com.robusta.photoweatherapp.presentation.repository.datalayer.model.photo.Photo

private lateinit var context: Context

class PhotosAdapter(
    var photos: List<Photo>,
    private var itemListener: RecyclerClick
) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photos[position], itemListener)
    }

    class ViewHolder(var binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            photo: Photo,
            itemListener: RecyclerClick
        ) {
            binding.apply {
                photo.apply {
                    ivPhoto.setImageBitmap(bitmap!!)
                }
                root.setOnClickListener {
                    itemListener.onImageClick(photo.bitmap)
                }
            }
        }
    }

}