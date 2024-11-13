package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.BestProductAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.BestDealsAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.SpecialProductsAdapter
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
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
    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRv()
        setupBestDealsRv()
        setupBaseProductsRv()

        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest {
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
            viewModel.bestDeals.collectLatest {
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
            viewModel.bestProducts.collectLatest {
                Utilities.ResourceOperation(it,
                    {
                        showLoading()
                    },
                    {
                        bestProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    },
                    {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Utilities.showToast(requireContext(), it.message.toString())
                    })
            }
        }
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
        }
    }

    private fun setupBaseProductsRv()
    {
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun setupBestDealsRv()
    {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDeals.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }
}