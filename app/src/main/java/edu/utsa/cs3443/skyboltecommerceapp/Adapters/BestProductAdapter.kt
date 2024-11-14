package edu.utsa.cs3443.skyboltecommerceapp.Adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.utsa.cs3443.skyboltecommerceapp.Data.Product
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.databinding.RvProductItemBinding

class BestProductAdapter : RecyclerView.Adapter<BestProductAdapter.ViewHolder>()
{
    inner class ViewHolder(private val binding: RvProductItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("DefaultLocale", "SetTextI18n")
        fun bind(product: Product)
        {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                product.offerPercentage?.let {
                    val remainingPricePercentage = 1 - it
                    val priceFinal = remainingPricePercentage * product.price
                    tvNewPrice.text = Utilities.price(priceFinal)
                    tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                if(product.offerPercentage == null) tvNewPrice.visibility = View.INVISIBLE
                tvPrice.text =  Utilities.price(product.price)
                tvName.text = product.name
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
            RvProductItemBinding.inflate(
                LayoutInflater.from(parent.context)
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