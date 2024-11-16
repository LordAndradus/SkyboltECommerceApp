package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Adapters.AllOrdersAdapter
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.AllOrdersViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentOrdersBinding

@AndroidEntryPoint
class AllOrdersFragment: Fragment()
{
    private lateinit var binding: FragmentOrdersBinding
    val viewModel by viewModels<AllOrdersViewModel>()
    val ordersAdapter by lazy { AllOrdersAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        setupOrdersRV()

        viewModel.allOrders.handleScope(this)

        viewModel.allOrders.onSuccessIterator = {
            ordersAdapter.differ.submitList(it.data)
            if(it.data.isNullOrEmpty())
            {
                binding.tvEmptyOrders.visibility = View.VISIBLE
            }
        }

        ordersAdapter.onItemClick = {
            val action = AllOrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun setupOrdersRV()
    {
        binding.rvAllOrders.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = ordersAdapter
        }

        binding.imageCloseOrders.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}