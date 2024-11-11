package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Startup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Activities.ShoppingActivity
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentAccountOptionsBinding

/**
 * The fragment page that handles what action the user will do
 *
 * There are three choices
 * Log in -> Log into their account and access their shopping data
 * Register -> Create an account to store their shopping data
 * Skip for now -> Skip creating/logging in, and view what products are available
 */

@AndroidEntryPoint
class AccountOptionsFragment : Fragment(
    R.layout.fragment_account_options
) {
    private lateinit var binding: FragmentAccountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.LoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_accountOptionsFragment_to_loginFragment)
        }

        binding.RegisterButton.setOnClickListener {
            findNavController().navigate(R.id.action_accountOptionsFragment_to_registerFragment)
        }

        binding.SkipForNowButton.setOnClickListener {
            Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                //Pop LoginRegisterActivity from stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity((intent))
            }
        }
    }
}