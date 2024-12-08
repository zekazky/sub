package com.rizka.submission1intermediate.view.story

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rizka.submission1intermediate.data.response.ListStoryItem
import com.rizka.submission1intermediate.loginwithanimation.R
import com.rizka.submission1intermediate.loginwithanimation.databinding.ItemStoryBinding

class StoryAdapter(private val onClick: (ListStoryItem) -> Unit) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private val list = ArrayList<ListStoryItem>()

    fun setStories(stories: List<ListStoryItem>) {
        list.clear()
        list.addAll(stories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = list[position]
        holder.bind(story, onClick)
    }

    override fun getItemCount(): Int = list.size

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem, onClick: (ListStoryItem) -> Unit) {
            binding.tvItemName.text = story.name
            binding.tvItemDescription.text = story.description
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.tvItemPhoto)

            binding.root.setOnClickListener {
                onClick(story)
            }
        }


    }
}
