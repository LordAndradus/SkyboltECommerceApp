package edu.utsa.cs3443.skyboltecommerceapp;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

/**
 * This is the main class of the Application and will handle special rules and exemptions
 *
 * For now, the entire purpose of this class is to provide special privileges to Dagger hilt and Firebase
 * This is denoted by the @HiltAndroidApp which tells Dagger Hilt that it's appropriate to inject code
 */

@HiltAndroidApp
public class SkyboltApplication extends Application
{

}
