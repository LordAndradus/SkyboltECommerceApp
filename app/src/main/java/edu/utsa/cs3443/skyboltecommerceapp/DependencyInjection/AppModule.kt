package edu.utsa.cs3443.skyboltecommerceapp.DependencyInjection

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.utsa.cs3443.skyboltecommerceapp.Firebase.FirebaseCommon
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
     * @return Instance of Firebase Authentication
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance();

    /**
     * Attach firestore to Dagger Hilt at runtime
     *
     * @return Instance of firestore
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore

    /**
     * Attach Firebase Storage to Dagger Hilt at runtime
     *
     * @return Instance of Firebase' Storage
     */
    @Provides
    @Singleton
    fun provideStorage() = FirebaseStorage.getInstance().reference

    /**
     * Attach User Shared Preferences to keep data locally to Dagger Hilt at runtime
     *
     * For example, to skip the introduction fragment, we will keep a boolean value to determine if
     * the user is visiting for the first time or that they have logged in.
     *
     * @return Instance of Firebase' Storage
     */
    @Provides
    fun provideIntroductionSP(
        application: Application
    ) = application.getSharedPreferences(INTRODUCTION_SHARED_PREFERENCES, MODE_PRIVATE)

    /**
     * Attach Firebase Commons to Dagger Hilt at runtime
     *
     * Attaches the custom class that will use a common Firebase feature that interacts with products
     * More specifically, it will help when the user decides to add or remove products from cart
     *
     * @return Instance of Firebase Common for hilt views
     */
    @Provides
    @Singleton
    fun provideFirebaseCommon(
        firebaseAuthenticator: FirebaseAuth,
        firestore: FirebaseFirestore
    ) = FirebaseCommon(firestore, firebaseAuthenticator)
}