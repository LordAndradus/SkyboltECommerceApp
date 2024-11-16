package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Shopping

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.ColorsAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.SizesAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.ViewPager2Images
import edu.utsa.cs3443.skyboltecommerceapp.Data.CartProduct
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities.Companion.hideBottomNavigation
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.DetailsViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentShoppingProductDetailsBinding

@AndroidEntryPoint
class ProductDetailsFragment: Fragment()
{
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentShoppingProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private var selectedColor: Int ?= null
    private var selectedSize: String ?= null
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingProductDetailsBinding.inflate(inflater)

        hideBottomNavigation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRv()
        setupColorsRv()
        setupViewpagerRv()

        binding.ImageBack.setOnClickListener {
            findNavController().navigateUp()
        }

        sizesAdapter.onItemClick = {
            selectedSize = it
        }

        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        binding.AddToCart.setOnClickListener {
            if((selectedSize == null && sizesAdapter.itemCount != 0) || (selectedColor == null && colorsAdapter.itemCount != 0))
            {
                if(selectedColor == null && colorsAdapter.itemCount != 0) Utilities.showToast(requireContext(), "You must select a color!")
                if(selectedSize == null && sizesAdapter.itemCount != 0) Utilities.showToast(requireContext(), "You must select a size!")
                return@setOnClickListener
            }

            viewModel.addUpdateProductInCart(CartProduct(product, 1, selectedColor, selectedSize))
        }

        viewModel.addToCart.handleScope(this, binding.AddToCart, null, {
            binding.AddToCart.setBackgroundColor(resources.getColor(R.color.teal_200))
        })

        binding.apply {
            var percentMult: Float
            var priceFinal: Float = product.price

            if(product.offerPercentage != null)
            {
                percentMult = 1 - product.offerPercentage
                priceFinal = product.price * percentMult
            }

            tvProductTitle.text = product.name
            tvProductPrice.text = Utilities.price(priceFinal)
            tvProductDescription.text = product.description?.let { String.format(it) }

            tvProductPricePreoffer.text = Utilities.price(product.price)
            tvProductPricePreoffer.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            tvProductPricePreoffer.visibility = View.GONE

            if(priceFinal != product.price) tvProductPricePreoffer.visibility = View.VISIBLE

            if(product.colors.isNullOrEmpty()) tvColors.visibility = View.INVISIBLE
            if(product.sizes.isNullOrEmpty()) tvSize.visibility = View.INVISIBLE

            if(product.colors?.size == 1)
            {
                tvColors.visibility = View.INVISIBLE
                llColors.visibility = View.INVISIBLE
            }
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let { colorsAdapter.differ.submitList(it) }
        product.sizes?.let { sizesAdapter.differ.submitList(it) }
        product.offerPercentage?.let { }
    }

    private fun setupSizesRv()
    {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupColorsRv()
    {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupViewpagerRv()
    {
        binding.apply {
            ViewpagerProductImages.adapter = viewPagerAdapter
        }
    }

}