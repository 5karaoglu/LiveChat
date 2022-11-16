package com.besirkaraoglu.livechat.data.di

import com.besirkaraoglu.livechat.core.utils.RealtimeDatabaseUtils
import com.besirkaraoglu.livechat.data.repository.MessagesRepository
import com.besirkaraoglu.livechat.data.repository.UsersRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRealtimeDatabase(): FirebaseDatabase =
        Firebase.database

    @Provides
    @Singleton
    fun provideUsersRepository(database: FirebaseDatabase): UsersRepository =
        UsersRepository(
            database.reference.child(RealtimeDatabaseUtils.INSTANCE_USERS),
            database.reference.child(RealtimeDatabaseUtils.INSTANCE_LOCATION_RECORD))

    @Provides
    @Singleton
    fun provideMessagesRepository(database: FirebaseDatabase): MessagesRepository =
        MessagesRepository(
            database.reference.child(RealtimeDatabaseUtils.INSTANCE_MESSAGES))

}