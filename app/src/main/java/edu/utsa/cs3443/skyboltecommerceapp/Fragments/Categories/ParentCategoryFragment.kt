package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.BestProductAdapter
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.PRODUCT
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities.Companion.showBottomNavigation
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.CategoryViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentCategoryParentBinding
import kotlinx.coroutines.flow.collectLatest

/**
 * A base/parent class for Category Fragments to inherit from. Makes the whole process easier
 */

open class ParentCategoryFragment () : Fragment(
    R.layout.fragment_category_parent
) {
    private lateinit var binding: FragmentCategoryParentBinding
    protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter() }
    protected val bestProductAdapter: BestProductAdapter by lazy { BestProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryParentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOfferRV()
        setupProductRV()

        offerAdapter.onClick = {
            val b = Bundle().apply { putParcelable(PRODUCT, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,  b)
        }

        bestProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable(PRODUCT, it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,  b)
        }
    }

    fun showOfferLoading()
    {
        binding.offerLoadingBar.visibility = View.VISIBLE
    }

    fun hideOfferLoading()
    {
        binding.offerLoadingBar.visibility = View.GONE
    }

    fun showExploreLoading()
    {
        binding.exploreLoadingBar.visibility = View.VISIBLE
    }

    fun hideExploreLoading()
    {
        binding.exploreLoadingBar.visibility = View.GONE
    }

    open fun onOfferPagingRequest()
    {

    }

    open fun onBestPagingRequest()
    {

    }

    private fun setupProductRV()
    {
        binding.rvExploreProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter

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
                        onBestPagingRequest()
                    }
                }
            })
        }
    }

    private fun setupOfferRV()
    {
        binding.rvOffer.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter

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
                        onOfferPagingRequest()
                    }
                }
            })
        }
    }

    override fun onResume()
    {
        super.onResume()
        showBottomNavigation()
    }
}