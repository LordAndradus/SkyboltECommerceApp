package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Startup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Activities.ShoppingActivity
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.IntroductionViewModel
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentIntroductionBinding

/**
 * The introduction page, to introduce users to our app with some catchy phrases
 *
 * Mainly adds functionality to the start button
 */

@AndroidEntryPoint
class IntroductionFragment : Fragment(
    R.layout.fragment_introduction
) {
    private lateinit var binding: FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.navigate.collect {
                when(it)
                {
                    SHOPPING_ACTIVITY -> {
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            //Pop LoginRegisterActivity from stack
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity((intent))
                        }
                    }

                    R.id.accountOptionsFragment -> {
                        findNavController().navigate(it)
                    }

                    else -> Utilities.nop()
                }
            }
        }

        binding.StartButton.setOnClickListener{
            viewModel.startButtonClick()
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
        }
    }
}