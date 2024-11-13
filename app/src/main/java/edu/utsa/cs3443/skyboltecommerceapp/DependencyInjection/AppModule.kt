package edu.utsa.cs3443.skyboltecommerceapp.DependencyInjection

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.INTRODUCTION_SHARED_PREFERENCES
import javax.inject.Singleton

/**
 * The purpose of this object data-container is to specify the life-time of all the dependencies in the modules for this
 * application
 *
 * We will be using this container as a Singleton.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule
{
    /**
     * Attach the Authenticator service from Firebase to Dagger Hilt at runtime
     * 
     * @param null
     * @return Instance of Firebase Authentication
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance();


    /**
     * Attach firestore to Dagger Hilt at runtime
     *
     * @param null
     * @return Instance of firestore
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore

    @Provides
    fun provideIntroductionSP(
        application: Application
    ) = application.getSharedPreferences(INTRODUCTION_SHARED_PREFERENCES, MODE_PRIVATE)
}