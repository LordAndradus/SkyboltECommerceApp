package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.AddressAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.BillingProductsAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Data.Address
import edu.utsa.cs3443.skyboltecommerceapp.Data.CartProduct
import edu.utsa.cs3443.skyboltecommerceapp.Data.Order
import edu.utsa.cs3443.skyboltecommerceapp.Data.OrderStatus
import edu.utsa.cs3443.skyboltecommerceapp.Helper.HorizontalItemDecoration
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.BillingViewModel
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.OrderViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentBillingBinding

@AndroidEntryPoint
class BillingFragment: Fragment()
{
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingAdapter by lazy { BillingProductsAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f

    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater)

        products = args.product.toList()
        totalPrice = args.totalPrice

        billingViewModel.address.handleScope(this, binding.progressbarAddress)
        billingViewModel.address.onSuccessIterator = {
            addressAdapter.differ.submitList(it.data)
        }

        orderViewModel.orders.handleScope(this, binding.buttonPlaceOrder, null, {
            findNavController().navigateUp()
            Utilities.showSnackbar(requireView(), "Your order has been placed!")
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        setupBillingProductsRv()
        setupAddressRv()

        if(!args.payment)
        {
            binding.apply {
                buttonPlaceOrder.visibility = View.INVISIBLE
                totalBoxContainer.visibility = View.INVISIBLE
                middleLine.visibility = View.INVISIBLE
                bottomLine.visibility = View.INVISIBLE
            }
        }

        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        billingAdapter.differ.submitList(products)
        binding.tvTotalPrice.text = Utilities.price(totalPrice)

        addressAdapter.onClick = {
            selectedAddress = it

            if(!args.payment)
            {
                val b = Bundle().apply { putParcelable("address", selectedAddress) }
                findNavController().navigate(R.id.action_billingFragment_to_addressFragment, b)
            }
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if(selectedAddress == null)
            {
                Utilities.showToast(requireContext(), "Please select an address")
            }

            showOrderConfirmationDialog()
        }
    }

    private fun showOrderConfirmationDialog()
    {
        if(selectedAddress == null)
        {
            selectedAddress = addressAdapter.differ.currentList[0]
        }

        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order items")
            setMessage("Are you sure you want to place your order?")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Confirm") { dialog, _ ->
                val order = Order(
                    OrderStatus.PLACED,
                    totalPrice,
                    products,
                    selectedAddress!!
                )

                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }

        alertDialog.create()
        alertDialog.show()
    }

    private fun setupBillingProductsRv()
    {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = billingAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupAddressRv()
    {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    override fun onResume()
    {
        //Reset the view to the currently selected address
        selectedAddress?.let {
            var idx = 0
            for(i in addressAdapter.differ.currentList)
            {
                if(i == selectedAddress)
                {
                    addressAdapter.notifyItemChanged(idx)
                    break
                }

                idx++
            }

        }
        super.onResume()
    }
}