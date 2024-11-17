package edu.utsa.cs3443.skyboltecommerceapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.utsa.cs3443.skyboltecommerceapp.databinding.RvSizeItemBinding

class SizesAdapter: RecyclerView.Adapter<SizesAdapter.ViewHolder>()
{
    private var selectedPosition = 0

    inner class ViewHolder(val binding: RvSizeItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(size: String, position: Int)
        {
            binding.tvSize.text = size

            //Size is selected
            if(position == selectedPosition)
            {
                binding.apply {
                    rectShadow.visibility = View.VISIBLE
                }
            }
            //Size is not selected
            else
            {
                binding.apply {
                    rectShadow.visibility = View.INVISIBLE
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<String>()
    {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean
        {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean
        {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            RvSizeItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val size = differ.currentList[position]
        holder.bind(size, position)

        holder.itemView.setOnClickListener {
            //Change the previous selection back to normal
            if(selectedPosition >= 0) notifyItemChanged(selectedPosition)
            //Change to new selection
            selectedPosition = holder.adapterPosition
            notifyItemChanged(position)

            onItemClick?.invoke(size)
        }
    }

    override fun getItemCount(): Int
    {
        return differ.currentList.size
    }

    var onItemClick:((String) -> Unit)?=null
}