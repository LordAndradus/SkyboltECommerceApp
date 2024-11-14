package edu.utsa.cs3443.skyboltecommerceapp.Adapters;

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.utsa.cs3443.skyboltecommerceapp.Data.Product
import edu.utsa.cs3443.skyboltecommerceapp.databinding.RvBestDealsItemBinding;

class BestDealsAdapter: RecyclerView.Adapter<BestDealsAdapter.ViewHolder>()
{
    inner class ViewHolder(private val binding: RvBestDealsItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("DefaultLocale", "SetTextI18n")
        fun bind(product: Product)
        {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgBestDeal)
                product.offerPercentage?.let {
                    val remainingPricePercentage = 1 - it
                    val priceFinal = remainingPricePercentage * product.price
                    tvNewPrice.text = "$${String.format("%.02f", priceFinal)}"
                }
                tvOldPrice.text =  "$${product.price}"
                tvOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                tvDealProductName.text = product.name
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>()
    {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean
        {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean
        {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            RvBestDealsItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val product = differ.currentList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int
    {
        return differ.currentList.size
    }

}
