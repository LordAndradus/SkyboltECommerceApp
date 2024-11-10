package edu.utsa.cs3443.skyboltecommerceapp.DependencyInjection

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    //Returns the current dependency instance of Firebase, so that we can inject it in the RegisterViewModel constructor
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance();

    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore
}