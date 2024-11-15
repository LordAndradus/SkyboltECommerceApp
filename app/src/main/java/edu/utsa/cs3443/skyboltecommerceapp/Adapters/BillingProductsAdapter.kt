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
import edu.utsa.cs3443.skyboltecommerceapp.databinding.RvBillingProductsItemBinding

class BillingProductsAdapter: RecyclerView.Adapter<BillingProductsAdapter.ViewHolder>()
{
    inner class ViewHolder(val binding: RvBillingProductsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale", "SetTextI18n")
        fun bind(product: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(product.product.images[0]).into(imageCartProduct)
                tvProductCartName.text = product.product.name
                tvBillingProductQuantity.text = product.quantity.toString()

                val finalPrice = product.product.offerPercentage.getProductPrice(product.product.price)
                tvProductCartPrice.text = Utilities.price(finalPrice)

                imageCartProductColor.setImageDrawable(ColorDrawable(product.selectedColor?: Color.TRANSPARENT))
                tvCartProductSize.text = product.selectedSize?:"".also { rectImage.visibility = View.INVISIBLE }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            RvBillingProductsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val billingProduct = differ.currentList[position]
        holder.bind(billingProduct)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((CartProduct) -> Unit)? = null
}