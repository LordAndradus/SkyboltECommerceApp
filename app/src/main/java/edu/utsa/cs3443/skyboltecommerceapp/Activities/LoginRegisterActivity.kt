package edu.utsa.cs3443.skyboltecommerceapp.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import edu.utsa.cs3443.skyboltecommerceapp.R

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
    }
}