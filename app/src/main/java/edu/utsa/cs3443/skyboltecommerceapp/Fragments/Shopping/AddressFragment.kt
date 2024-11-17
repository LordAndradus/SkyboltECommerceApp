package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Shopping

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Data.Address
import edu.utsa.cs3443.skyboltecommerceapp.Helper.PhoneEntry
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.AddressViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentAddressBinding

@AndroidEntryPoint
class AddressFragment: Fragment()
{
    private lateinit var binding: FragmentAddressBinding
    val viewModel by viewModels<AddressViewModel>()
    val args by navArgs<AddressFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)

        viewModel.addressHandler.handleScope(this, binding.progressbarAddress,
            null, {findNavController().navigateUp()}, null, true)

        viewModel.addressCollection.handleScope(this, binding.progressbarAddress, { Utilities.showToast(requireContext(), "Successfully deleted address!")})

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val address = args.address

        if(address == null)
        {
            binding.buttonDelete.visibility = View.GONE
        }
        else
        {
            binding.apply {
                edAddressTitle.setText(address.addressTitle)
                edFullName.setText(address.fullName)
                edStreet.setText(address.street)
                edState.setText(address.state)
                edPhone.setText(address.phoneNumber)
                edCity.setText(address.city)
                edState.setText(address.state)
            }
        }

        binding.apply {
            buttonSave.setOnClickListener {
                val addressTitle = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = edState.text.toString()

                val address = Address(addressTitle, fullName, street, phone, city, state)

                if(args.address == null)
                {
                    viewModel.addAddress(address)
                }
                else
                {
                    viewModel.setAddress(args.address!!, address)
                    findNavController().navigateUp()
                }
            }

            buttonDelete.setOnClickListener {
                viewModel.deleteAddress(args.address!!)
                findNavController().navigateUp()
            }

            imageAddressClose.setOnClickListener {
                findNavController().navigateUp()
            }

            edPhone.addTextChangedListener(PhoneEntry(edPhone))
            edPhone.setOnKeyListener { _, keyCode, event ->
                if(keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN)
                {
                    if(edPhone.text.isEmpty()) return@setOnKeyListener true
                    if(edPhone.text.toString().length == 2)
                    {
                        edPhone.setText("")
                        return@setOnKeyListener true
                    }
                    val currentText: String = edPhone.text.toString().replace("[\\D]".toRegex(), "")
                    val deleted = currentText.substring(0, currentText.length - 1)
                    edPhone.setText(deleted)

                    true
                }
                else
                {
                    false
                }
            }
        }
    }
}