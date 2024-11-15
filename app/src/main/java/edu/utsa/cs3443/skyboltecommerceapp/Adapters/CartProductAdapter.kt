package edu.utsa.cs3443.skyboltecommerceapp.Adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.utsa.cs3443.skyboltecommerceapp.Data.CartProduct
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities.Companion.getProductPrice
import edu.utsa.cs3443.skyboltecommerceapp.databinding.CartProductItemBinding

class CartProductAdapter: RecyclerView.Adapter<CartProductAdapter.ViewHolder>()
{
    inner class ViewHolder(val binding: CartProductItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("DefaultLocale")
        fun bind(cartProduct: CartProduct)
        {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(ivCartProductImage)
                tvCartProductTitle.text = cartProduct.product.name
                tvCartProductQuantity.text = cartProduct.quantity.toString()

                val priceFinal = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                tvCartProductPrice.text = Utilities.price(priceFinal)

                if(priceFinal == cartProduct.product.price)
                {
                    tvCartProductPreofferPrice.visibility = View.INVISIBLE
                }
                else
                {
                    tvCartProductPreofferPrice.text = Utilities.price(cartProduct.product.price)
                }

                ivCartProductColor.setImageDrawable(ColorDrawable(cartProduct.selectedColor?: Color.TRANSPARENT))
                tvSize.text = cartProduct.selectedSize?:"".also { rectImage.visibility = View.INVISIBLE }
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean
        {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean
        {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)

        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartProduct)
        }

        holder.binding.ivAddToCartProduct.setOnClickListener {
            onAddClick?.invoke(cartProduct)
        }

        holder.binding.ivRemoveFromCartProduct.setOnClickListener {
            onRemoveClick?.invoke(cartProduct)
        }
    }

    override fun getItemCount(): Int
    {
        return differ.currentList.size
    }

    var onProductClick: ((CartProduct) -> Unit) ?= null
    var onAddClick: ((CartProduct) -> Unit) ?= null
    var onRemoveClick: ((CartProduct) -> Unit) ?= null
}