package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Shopping

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Activities.LoginRegisterActivity
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities.Companion.showBottomNavigation
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.ProfileViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentProfileBinding

@AndroidEntryPoint
class ProfileFragment : Fragment()
{
    private lateinit var binding: FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)

        //May or may not have to set the progress bar to GONE
        viewModel.user.handleScope(this, binding.progressbarSettings)
        viewModel.user.onSuccessIterator = {
            Glide.with(requireView()).load(it.data!!.imagePath)
                .error(ColorDrawable(Color.BLACK))
                .into(binding.imageUser)

            binding.tvUserName.text = "${it.data.firstName} ${it.data.lastName}"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }

        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }

        binding.linearBilling.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToBillingFragment(0f, emptyArray(), false)
            findNavController().navigate(action)
        }

        binding.linearLogOut.setOnClickListener {
            viewModel.logout()

            val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.tvVersion.text = "Version 0.5.0"
    }

    override fun onResume()
    {
        super.onResume()
        showBottomNavigation()
    }
}