package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.BillingProductsAdapter
import edu.utsa.cs3443.skyboltecommerceapp.Data.OrderStatus
import edu.utsa.cs3443.skyboltecommerceapp.Helper.VerticalItemDecoration
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentOrderDetailBinding

class OrderDetailFragment: Fragment()
{
    private lateinit var binding: FragmentOrderDetailBinding
    private val billingAdapter by lazy { BillingProductsAdapter() }
    private val args by navArgs<OrderDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val order = args.order

        setupOrderRV()

        binding.apply {
            tvOrderId.setText("0rder #${order.orderID}")

            stepView.setSteps(
                mutableListOf(
                    OrderStatus.PLACED.toString(),
                    OrderStatus.CONFIRMED.toString(),
                    OrderStatus.SHIPPED.toString(),
                    OrderStatus.DELIVERED.toString()
                )
            )

            var currentOrderStatus = when(order.orderStatus) {
                OrderStatus.PLACED -> 0
                OrderStatus.CONFIRMED -> 1
                OrderStatus.SHIPPED -> 2
                OrderStatus.DELIVERED -> 3
                else -> 0
            }

            if(order.orderStatus == OrderStatus.CANCELLED)
            {
                stepView.setSteps(mutableListOf(OrderStatus.CANCELLED.toString().substring(0, 1) + OrderStatus.CANCELLED.toString().substring(1).lowercase()))
                currentOrderStatus = 0
            }

            if(order.orderStatus == OrderStatus.RETURNED)
            {
                stepView.setSteps(mutableListOf(OrderStatus.RETURNED.toString().substring(0, 1) + OrderStatus.RETURNED.toString().substring(1).lowercase()))
                currentOrderStatus = 0
            }

            stepView.go(currentOrderStatus, true)

            if(currentOrderStatus == stepView.stepCount - 1)
            {
                stepView.done(true)
            }

            tvFullName.text = order.address.fullName
            tvAddress.text = "${order.address.street}\n${order.address.city}, ${order.address.state}"
            tvPhoneNumber.text = order.address.phoneNumber

            tvTotalPrice.text = Utilities.price(order.totalPrice)
        }

        billingAdapter.differ.submitList(order.products)
    }

    private fun setupOrderRV()
    {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = billingAdapter
            addItemDecoration(VerticalItemDecoration())
        }

        binding.imageCloseOrder.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}