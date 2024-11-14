package edu.utsa.cs3443.skyboltecommerceapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.ExploreProductsAdapter.ViewHolder
import edu.utsa.cs3443.skyboltecommerceapp.Data.Product
import edu.utsa.cs3443.skyboltecommerceapp.databinding.RvProductItemBinding
import edu.utsa.cs3443.skyboltecommerceapp.databinding.ViewpagerImageItemBinding

class ViewPager2Images: RecyclerView.Adapter<ViewPager2Images.ViewHolder>()
{
    inner class ViewHolder(val binding: ViewpagerImageItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(imagePath: String)
        {
            Glide.with(itemView).load(imagePath).into(binding.ivProductDetails)
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
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val url = differ.currentList[position]
        holder.bind(url)
    }

    override fun getItemCount(): Int
    {
        return differ.currentList.size
    }
}