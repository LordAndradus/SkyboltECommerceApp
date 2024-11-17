package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Settings

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Data.User
import edu.utsa.cs3443.skyboltecommerceapp.Dialogs.setupBottomSheetDialog
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.UserAccountViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentUserAccountBinding

@AndroidEntryPoint
class UserAccountFragment : Fragment()
{
    private lateinit var binding: FragmentUserAccountBinding
    private val viewModel by viewModels<UserAccountViewModel>()

    private var imageUri: Uri ?= null

    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userThis.handleScope(this, null,
            { showUserLoading() }, { hideUserLoading() }, { hideUserLoading() })
        viewModel.userThis.onSuccessIterator = {
            showUserInformation(it.data!!)
        }

        viewModel.updateInfo.handleScope(this, null,
            {binding.buttonSave.startAnimation()}, {
                binding.progressbarAccount.visibility = View.INVISIBLE
                binding.buttonSave.revertAnimation()
                findNavController().navigateUp()
            })

        //Lifecycle listener when resetting password
        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect {
                Utilities.ResourceOperation<String>(it,
                    {

                    },
                    {
                        Snackbar.make(requireView(), "Reset link was sent to your email", Snackbar.LENGTH_LONG).show()
                    },
                    {
                        Snackbar.make(requireView(), "ERROR: ${it.message}", Snackbar.LENGTH_LONG).show()
                    })
            }
        }

        binding.tvUpdatePassword.setOnClickListener {
            setupBottomSheetDialog { Email ->
                viewModel.resetPassword(Email)
            }
        }

        binding.buttonSave.setOnClickListener {
            binding.apply {
                val firstName = edFirstName.text.toString().trim()
                val lastName = edLastName.text.toString().trim()
                val email = edEmail.text.toString().trim()
                val user = User(firstName, lastName, email)

                viewModel.updateUser(user, imageUri)
            }
        }

        binding.imageCloseUserAccount.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        imageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            imageUri = it.data?.data
            Glide.with(this).load(imageUri).into(binding.imageUser)
        }
    }

    private fun hideUserLoading()
    {
        binding.apply {
            progressbarAccount.visibility = View.GONE
            imageUser.visibility = View.VISIBLE
            imageEdit.visibility = View.VISIBLE
            edFirstName.visibility = View.VISIBLE
            edLastName.visibility = View.VISIBLE
            edEmail.visibility = View.VISIBLE
            tvUpdatePassword.visibility = View.VISIBLE
            buttonSave.visibility = View.VISIBLE
        }
    }

    private fun showUserLoading()
    {
        binding.apply {
            progressbarAccount.visibility = View.VISIBLE
            imageUser.visibility = View.INVISIBLE
            imageEdit.visibility = View.INVISIBLE
            edFirstName.visibility = View.INVISIBLE
            edLastName.visibility = View.INVISIBLE
            edEmail.visibility = View.INVISIBLE
            tvUpdatePassword.visibility = View.INVISIBLE
            buttonSave.visibility = View.INVISIBLE
        }
    }

    private fun showUserInformation(data: User)
    {
        binding.apply {
            Glide.with(this@UserAccountFragment).load(data.imagePath).error(ColorDrawable(Color.BLACK)).into(imageUser)
            edFirstName.setText(data.firstName)
            edLastName.setText(data.lastName)
            edEmail.setText(data.email)
        }
    }
}