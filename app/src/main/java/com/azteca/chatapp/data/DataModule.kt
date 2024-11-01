package com.azteca.chatapp.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    /* PROVEEMOS firebase para evitar tantas instancias */

    @Singleton
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseMessage() = Firebase.messaging
}