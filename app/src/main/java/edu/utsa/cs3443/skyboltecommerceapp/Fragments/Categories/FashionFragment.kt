package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Util.Categories
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.CategoryViewModel
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.Factory.BaseCategoryViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class FashionFragment : ParentCategoryFragment()
{
    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore, Categories.Fashion)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest {
                Utilities.ResourceOperation(it,
                    {
                        showOfferLoading()
                    },
                    {
                        offerAdapter.differ.submitList(it.data)
                        hideOfferLoading()
                    },
                    {
                        hideOfferLoading()
                        Utilities.showSnackbar(requireView(), it.message.toString())
                    })
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                Utilities.ResourceOperation(it,
                    {
                        showExploreLoading()
                    },
                    {
                        bestProductAdapter.differ.submitList(it.data)
                        hideExploreLoading()
                    },
                    {
                        Utilities.showSnackbar(requireView(), it.message.toString())
                        hideExploreLoading()
                    })
            }
        }
    }

    override fun onBestPagingRequest()
    {

    }

    override fun onOfferPagingRequest()
    {

    }
}