package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Shopping

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.HomeViewpagerAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.AccessoriesFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.ElectronicsFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.FashionFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.FoodFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.FurnitureFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.MainCategoryFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.MedicalFragment
import edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories.PetsFragment
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentShoppingHomeBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * The main fragment
 *
 * Essentially, it sets up everything on the main page view such as best deals, hot products, etc.
 */

class HomeFragment : Fragment(
    R.layout.fragment_shopping_home
) {
    private lateinit var binding: FragmentShoppingHomeBinding

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingHomeBinding.inflate(inflater)

        cameraExecutor = Executors.newSingleThreadExecutor()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val CategoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            FashionFragment(),
            ElectronicsFragment(),
            AccessoriesFragment(),
            FurnitureFragment(),
            MedicalFragment(),
            FoodFragment(),
            PetsFragment()
        )

        binding.ViewpagerHome.isUserInputEnabled = false

        val viewPager2Adapter = HomeViewpagerAdapter(CategoriesFragments, childFragmentManager, lifecycle)
        binding.ViewpagerHome.adapter = viewPager2Adapter

        TabLayoutMediator(binding.TabLayout, binding.ViewpagerHome) { tab, position ->
            when(position)
            {
                0 -> tab.text = "Home"
                1 -> tab.text = "Fashion"
                2 -> tab.text = "Electronics"
                3 -> tab.text = "Accessories"
                4 -> tab.text = "Furniture"
                5 -> tab.text = "Medical"
                6 -> tab.text = "Food"
                7 -> tab.text = "Pets"
            }
        }.attach()

        binding.scanQRCode.setOnClickListener {
            Log.d("Camera", "Trying to open ")
            startScanner()
        }

        binding.SearchBar.setOnClickListener {
            //TODO: Pop home from the stack os that you can tap on home to go back, or open the search fragment somehow
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun startScanner()
    {
        val permission = Manifest.permission.CAMERA
        if(activity?.let { ContextCompat.checkSelfPermission(it, permission) } == PackageManager.PERMISSION_GRANTED)
        {
            openCameraScanner()
        }
        else
        {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(permission), 101) }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Utilities.showToast(requireContext(), "Camera permissions granted")
            }
            else
            {
                Utilities.showToast(requireContext(), "Camera permissions denied")
            }
        }
    }

    private fun openCameraScanner()
    {
        findNavController().navigate(R.id.action_homeFragment_to_cameraScannerFragment)
    }
}