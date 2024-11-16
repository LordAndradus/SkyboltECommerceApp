package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Util.Categories
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.CategoryViewModel
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.Factory.BaseCategoryViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class FoodFragment : ParentCategoryFragment()
{
    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore, Categories.Food)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewModel.offerProducts.handleScope(this, binding.offerLoadingBar)
        viewModel.offerProducts.onSuccessIterator = {
            offerAdapter.differ.submitList(it.data)
        }

        viewModel.bestProducts.handleScope(this, binding.exploreLoadingBar)
        viewModel.bestProducts.onSuccessIterator = {
            bestProductAdapter.differ.submitList(it.data)
        }
    }

    override fun onBestPagingRequest()
    {

    }

    override fun onOfferPagingRequest()
    {

    }
}