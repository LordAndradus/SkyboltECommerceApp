package edu.utsa.cs3443.skyboltecommerceapp.Activities;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint

import edu.utsa.cs3443.skyboltecommerceapp.R;
import edu.utsa.cs3443.skyboltecommerceapp.databinding.ActivityShoppingBinding;

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

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Setup the home bar to navigate via categories
        val navController = findNavController(R.id.ShoppingHostFragment)
        binding.BottomNavigator.setupWithNavController(navController)
    }
}