package edu.utsa.cs3443.skyboltecommerceapp.Adapters

import android.graphics.Paint
import android.view.View
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import edu.utsa.cs3443.skyboltecommerceapp.Data.Product
import edu.utsa.cs3443.skyboltecommerceapp.databinding.RvProductItemBinding

class ExploreProductsAdapter : ParentRecyclerViewAdapter<RvProductItemBinding>(
    RvProductItemBinding::inflate
){
    override fun bindFunction(binding: RvProductItemBinding, vh: ViewHolder, product: Product) {
        binding.apply {
            Glide.with(vh.getImageView()).load(product.images[0]).into(imgProduct)
            product.offerPercentage?.let {
                val remainingPricePercentage = 1 - it
                val priceFinal = remainingPricePercentage * product.price
                tvNewPrice.text = "$${String.format("%.02f", priceFinal)}"
                tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            if(product.offerPercentage == null) tvNewPrice.visibility = View.INVISIBLE
            tvPrice.text =  "$${product.price}"
            tvName.text = product.name
        }
    }
}
