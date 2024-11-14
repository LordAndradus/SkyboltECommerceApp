package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.BestProductAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.BestDealsAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.SpecialProductsAdapter
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities.Companion.showBottomNavigation
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.MainCategoryViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentCategoryMainBinding
import kotlinx.coroutines.flow.collectLatest

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
    private val viewModel by viewModels<MainCategoryViewModel>()

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
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,  b)
        }

        bestDealsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,  b)
        }

        bestProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,  b)
        }

        exploreProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,  b)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.specialProductsLister.productList.collectLatest {
                Utilities.ResourceOperation(it,
                    {
                        showLoading()
                    },
                    {
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    },
                    {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Utilities.showToast(requireContext(), it.message.toString())
                    })
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestDealsLister.productList.collectLatest {
                Utilities.ResourceOperation(it,
                    {
                        showLoading()
                    },
                    {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    },
                    {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Utilities.showToast(requireContext(), it.message.toString())
                    })
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProductsLister.productList.collectLatest {
                Utilities.ResourceOperation(it,
                    {
                        binding.BestProductsLoadingProgressBar.visibility = View.VISIBLE
                    },
                    {
                        bestProductAdapter.differ.submitList(it.data)
                        binding.BestProductsLoadingProgressBar.visibility = View.GONE
                    },
                    {
                        binding.BestProductsLoadingProgressBar.visibility = View.GONE
                        Log.e(TAG, it.message.toString())
                        Utilities.showToast(requireContext(), it.message.toString())
                    })
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.allProductsLister.productList.collectLatest {
                Utilities.ResourceOperation(it,
                    {
                        binding.BestProductsLoadingProgressBar.visibility = View.VISIBLE
                    },
                    {
                        exploreProductsAdapter.differ.submitList(it.data)
                        binding.BestProductsLoadingProgressBar.visibility = View.GONE
                    },
                    {
                        binding.BestProductsLoadingProgressBar.visibility = View.GONE
                        Log.e(TAG, it.message.toString())
                        Utilities.showToast(requireContext(), it.message.toString())
                    })
            }
        }

        binding.NestedScrollCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { view, _, scrollY, scrollX, _ ->
            if(view.getChildAt(0).bottom <= view.height + scrollY)
            {
                //We reached the bottom of the scroll view
                viewModel.allProductsLister.fetch()
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
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter

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