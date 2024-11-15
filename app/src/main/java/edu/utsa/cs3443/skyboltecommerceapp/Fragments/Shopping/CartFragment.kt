package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.CartProductAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Firebase.FirebaseCommon
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.PRODUCT
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.Util.VerticalItemDecoration
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.CartViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentShoppingCartBinding
import kotlinx.coroutines.flow.collectLatest

class CartFragment : Fragment(
    R.layout.fragment_shopping_cart
) {
    private lateinit var binding: FragmentShoppingCartBinding
    private val cartAdapter by lazy { CartProductAdapter() }
    private val viewModel by activityViewModels<CartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        setupCartRV()

        lifecycleScope.launchWhenStarted {
            viewModel.productsPrice.collectLatest { price ->
                price?.let {
                    binding.tvTotalPrice.text = Utilities.price(price as Float)
                }
            }
        }

        cartAdapter.onProductClick = {
            val b = Bundle().apply { putParcelable(PRODUCT, it.product) }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment, b)
        }

        cartAdapter.onAddClick = {
            viewModel.changeQuantity(it, FirebaseCommon.QuantityChanging.INCREASED)
        }

        cartAdapter.onRemoveClick = {
            viewModel.changeQuantity(it, FirebaseCommon.QuantityChanging.DECREASED)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest {
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from cart")
                    setMessage("Do you wish to delete this item from your cart?")
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Delete") { dialog, _ ->
                        viewModel.deleteCartProduct(it)
                        dialog.dismiss()
                    }
                }

                alertDialog.create()
                alertDialog.show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                Utilities.ResourceOperation(it,
                    {
                        binding.progressBarCart.visibility = View.VISIBLE
                    },
                    {
                        binding.progressBarCart.visibility = View.INVISIBLE

                        if(it.data!!.isEmpty())
                        {
                            showEmptyCart()
                            hideOtherViews()
                        }
                        else
                        {
                            hideEmptyCart()
                            showOtherViews()
                            cartAdapter.differ.submitList(it.data)
                        }
                    },
                    {
                        binding.progressBarCart.visibility = View.INVISIBLE
                        Utilities.showToast(requireContext(), it.message.toString())
                    })
            }
        }
    }

    private fun showOtherViews()
    {
        binding.apply {
            rvCartView.visibility = View.VISIBLE
            totalContainer.visibility = View.VISIBLE
            finishCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideOtherViews()
    {
        binding.apply {
            rvCartView.visibility = View.GONE
            totalContainer.visibility = View.GONE
            finishCheckout.visibility = View.GONE
        }
    }

    private fun hideEmptyCart()
    {
        binding.apply {
            layoutCartEmpty.visibility = View.GONE
        }
    }

    private fun showEmptyCart()
    {
        binding.apply {
            layoutCartEmpty.visibility = View.VISIBLE
        }
    }

    private fun setupCartRV()
    {
        binding.rvCartView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}