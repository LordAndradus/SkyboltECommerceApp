package edu.utsa.cs3443.skyboltecommerceapp.Activities;


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.CartViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.ActivityShoppingBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * The main soup and potatoes of the shopping experience
 *
 * TODO: The user can shop and browse at their leisure on this interface. Might use more fragments for a nav-map
 */
@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity()
{
    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Setup the home bar to navigate via categories
        val navController = findNavController(R.id.ShoppingHostFragment)
        binding.BottomNavigator.setupWithNavController(navController)

        viewModel.cartProducts.onSuccessIterator = {
            val count = it.data?.size ?: 0
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.BottomNavigator)
            bottomNavigationView.getOrCreateBadge(R.id.cartFragment).apply {
                number = count
                backgroundColor = resources.getColor(R.color.g_blue)
            }
        }

        lifecycleScope.launch {
            viewModel.cartProducts.signal.collectLatest {
                viewModel.cartProducts.onSuccessIterator?.invoke(it)
            }
        }
    }
}