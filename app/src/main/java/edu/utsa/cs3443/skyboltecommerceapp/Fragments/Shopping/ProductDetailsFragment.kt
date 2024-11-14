package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.ColorsAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.SizesAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.ViewPager2Images
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentShoppingProductDetailsBinding

class ProductDetailsFragment: Fragment()
{
    private lateinit var binding: FragmentShoppingProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorAdapter by lazy { ColorsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingProductDetailsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}