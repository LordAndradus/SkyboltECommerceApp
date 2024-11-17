package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.BestDealsAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.BestProductAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.SpecialProductsAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Helper.LinearSnapLeft
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.PRODUCT
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities.Companion.showBottomNavigation
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.MainCategoryViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentCategoryMainBinding

private val TAG = "Special Product"

/**
 * Fragment for the main shopping experience
 */
@AndroidEntryPoint
class MainCategoryFragment : Fragment(
    R.layout.fragment_category_main
) {
    private lateinit var  binding: FragmentCategoryMainBinding
    private lateinit var specialProductsAdapter : SpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductAdapter: BestProductAdapter
    private lateinit var exploreProductsAdapter: BestProductAdapter
    val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRv()
        setupBestDealsRv()
        setupBestProductsRv()
        setupExploreProductsRv()

        specialProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable(PRODUCT, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,  b)
        }

        specialProductsAdapter.onButtonClick = {
            viewModel.addProductToCart(it)
        }

        bestDealsAdapter.onClick = {
            val b = Bundle().apply { putParcelable(PRODUCT, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,  b)
        }

        bestDealsAdapter.onButtonClick = {
            val b = Bundle().apply { putParcelable(PRODUCT, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,  b)
        }

        bestProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable(PRODUCT, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,  b)
        }

        exploreProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable(PRODUCT, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,  b)
        }

        viewModel.specialProductsLister.handleScope(this, binding.MainCategoryProgressBar, true)
        viewModel.specialProductsLister.onSuccessIterator = {
            specialProductsAdapter.differ.submitList(it.data)
        }

        viewModel.bestDealsLister.handleScope(this, binding.MainCategoryProgressBar, true)
        viewModel.bestDealsLister.onSuccessIterator = {
            bestDealsAdapter.differ.submitList(it.data)
        }

        viewModel.bestProductsLister.handleScope(this, binding.MainCategoryProgressBar, true)
        viewModel.bestProductsLister.onSuccessIterator = {
            bestProductAdapter.differ.submitList(it.data)
        }

        viewModel.exploreProductsLister.handleScope(this, binding.MainCategoryProgressBar, true)
        viewModel.exploreProductsLister.onSuccessIterator = {
            exploreProductsAdapter.differ.submitList(it.data)
        }

        binding.NestedScrollCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { view, _, scrollY, scrollX, _ ->
            if(view.getChildAt(0).bottom <= view.height + scrollY)
            {
                //We reached the bottom of the scroll view
                viewModel.exploreProductsLister.fetch()
            }
        })
    }

    private fun showLoading()
    {
        binding.MainCategoryProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading()
    {
        binding.MainCategoryProgressBar.visibility = View.GONE
    }

    private fun setupSpecialProductRv()
    {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductsAdapter
            val snapItem = LinearSnapLeft()
            snapItem.attachToRecyclerView(binding.rvSpecialProducts)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
                {
                    super.onScrolled(recyclerView, dx, dy)

                    val lM = (layoutManager as LinearLayoutManager)
                    val lastItemPosition = lM.findLastCompletelyVisibleItemPosition()
                    val totalItemCount = lM.itemCount

                    if(lastItemPosition == totalItemCount - 1)
                    {
                        //End of list reached
                        viewModel.specialProductsLister.fetch()
                    }
                }
            })
        }
    }

    private fun setupBestDealsRv()
    {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDeals.apply {
            layoutManager = GridLayoutManager(requireContext(), 3, LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
            val snapItem = LinearSnapLeft()
            snapItem.attachToRecyclerView(binding.rvBestDeals)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
                {
                    super.onScrolled(recyclerView, dx, dy)

                    val lM = (layoutManager as LinearLayoutManager)
                    val lastItemPosition = lM.findLastCompletelyVisibleItemPosition()
                    val totalItemCount = lM.itemCount

                    Log.d("Recycler View Scrolling",
                        String.format("Last position %d => Total Count %d\n", lastItemPosition, totalItemCount)
                    )

                    if(lastItemPosition == totalItemCount - 1)
                    {
                        Log.d("Recycler View Scrolling", "Fetching Products")

                        //End of list reached
                        viewModel.bestDealsLister.fetch()
                    }
                }
            })
        }
    }

    private fun setupBestProductsRv()
    {
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun setupExploreProductsRv()
    {
        exploreProductsAdapter = BestProductAdapter()
        binding.rvExploreProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
            adapter = exploreProductsAdapter
        }
    }

    override fun onResume()
    {
        super.onResume()
        showBottomNavigation()
    }
}