package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.BestProductAdapter
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentCategoryParentBinding

/**
 * A base/parent class for Category Fragments to inherit from. Makes the whole process easier
 */

open class ParentCategoryFragment : Fragment(
    R.layout.fragment_category_parent
) {
    private lateinit var binding: FragmentCategoryParentBinding
    private lateinit var offerAdapter: BestProductAdapter
    private lateinit var bestProductAdapter: BestProductAdapter

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
    }

    private fun setupProductRV()
    {
        bestProductAdapter = BestProductAdapter()
        binding.rvExploreProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun setupOfferRV()
    {
        offerAdapter = BestProductAdapter()
        binding.rvExploreProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
        }
    }
}