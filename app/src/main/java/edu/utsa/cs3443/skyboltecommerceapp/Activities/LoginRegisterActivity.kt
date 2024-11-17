package edu.utsa.cs3443.skyboltecommerceapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.R

/**
 * This class serves the special purpose of being the entrypoint to the app, it instantiates Dagger hilt APIs
 *
 * Additionally, it will load the first fragment "fragment_introduction", so that the user can start their journey
 */

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
    }
}