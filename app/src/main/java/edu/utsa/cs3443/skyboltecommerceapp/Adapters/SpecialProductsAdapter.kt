package edu.utsa.cs3443.skyboltecommerceapp.Adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.utsa.cs3443.skyboltecommerceapp.Data.Product
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.databinding.RvSpecialItemBinding

class SpecialProductsAdapter: RecyclerView.Adapter<SpecialProductsAdapter.ViewHolder>()
{
    inner class ViewHolder(private val binding: RvSpecialItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("DefaultLocale")
        fun bind(product: Product)
        {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imageSpecialRVItem)
                tvSpecialProductName.text = product.name
                tvSpecialProductPrice.text = Utilities.price(product.price)
                product.offerPercentage?.let {
                    val percentOff = 1 - product.offerPercentage
                    val finalPrice = product.price * percentOff
                    tvSpecialProductPrice.text = Utilities.price(finalPrice)
                    tvSpecialProductPrice.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                    tvSpecialProductPrice.setTypeface(null, Typeface.ITALIC)
                }
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean
        {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean
        {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            RvSpecialItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int
    {
        return differ.currentList.size
    }

    var onClick: ((Product) -> Unit) ?= null
}