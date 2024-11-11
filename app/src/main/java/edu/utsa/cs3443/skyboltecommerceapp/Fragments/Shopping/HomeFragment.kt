package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.HomeViewpagerAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.AccessoriesFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.ElectronicsFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.FashionFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.FurnitureFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.MainCategoryFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.MedicalFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.PetsFragment
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentShoppingHomeBinding

class HomeFragment : Fragment(
    R.layout.fragment_shopping_home
) {
    private lateinit var binding: FragmentShoppingHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val CategoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            FashionFragment(),
            ElectronicsFragment(),
            AccessoriesFragment(),
            FurnitureFragment(),
            MedicalFragment(),
            PetsFragment()
        )

        val viewPager2Adapter = HomeViewpagerAdapter(CategoriesFragments, childFragmentManager, lifecycle)
        binding.ViewpagerHome.adapter = viewPager2Adapter

        TabLayoutMediator(binding.TabLayout, binding.ViewpagerHome) { tab, position ->
            when(position)
            {
                0 -> tab.text = "Main"
                1 -> tab.text = "Fashion"
                2 -> tab.text = "Electronics"
                3 -> tab.text = "Accessories"
                4 -> tab.text = "Furniture"
                5 -> tab.text = "Medical"
                6 -> tab.text = "Pets"
            }
        }.attach()
    }
}