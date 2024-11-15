package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Data.Address
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.AddressViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentAddressBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment: Fragment()
{
    private lateinit var binding: FragmentAddressBinding
    val viewModel by viewModels<AddressViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonSave.setOnClickListener {
                val addressTitle = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = edState.text.toString()

                val address = Address(addressTitle, fullName, street, phone, city, state)

                viewModel.addAddress(address)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                Utilities.ResourceOperation(it,
                    {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    },
                    {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        findNavController().navigateUp()
                    },
                    {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        Utilities.showToast(requireContext(), it.message.toString())
                    })
            }
        }

        lifecycleScope.launch {
            viewModel.error.collectLatest {
                Utilities.showToast(requireContext(), it)
            }
        }
    }
}