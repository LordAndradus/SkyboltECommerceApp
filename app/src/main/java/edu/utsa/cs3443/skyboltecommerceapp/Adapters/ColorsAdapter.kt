package edu.utsa.cs3443.skyboltecommerceapp.Adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.utsa.cs3443.skyboltecommerceapp.databinding.RvColorItemBinding

class ColorsAdapter: RecyclerView.Adapter<ColorsAdapter.ViewHolder>()
{
    private var selectedPosition = 0

    inner class ViewHolder(val binding: RvColorItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(color: Int, position: Int)
        {
            val imageDrawable = ColorDrawable(color)
            binding.imageColor.setImageDrawable(imageDrawable)

            //Color is selected
            if(position == selectedPosition)
            {
                binding.apply {
                    colorShadow.visibility = View.VISIBLE
                    imagePicked.visibility = View.VISIBLE

                    val isBackgroundDark: Boolean = (1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255) >= 0.5f
                    if(isBackgroundDark) imagePicked.setColorFilter(Color.WHITE)
                    else imagePicked.setColorFilter(Color.BLACK)
                }
            }
            //Color is not selected
            else
            {
                binding.apply {
                    colorShadow.visibility = View.INVISIBLE
                    imagePicked.visibility = View.INVISIBLE
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Int>()
    {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean
        {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean
        {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            RvColorItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val color = differ.currentList[position]
        holder.bind(color, position)

        holder.itemView.setOnClickListener {
            //Change the previous selection back to normal
            if(selectedPosition >= 0) notifyItemChanged(selectedPosition)
            //Change to new selection
            selectedPosition = holder.adapterPosition
            notifyItemChanged(position)

            onItemClick?.invoke(color)
        }
    }

    override fun getItemCount(): Int
    {
        return differ.currentList.size
    }

    var onItemClick:((Int) -> Unit)?=null
}