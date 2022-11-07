package com.besirkaraoglu.livechat.data.repository

import com.besirkaraoglu.livechat.core.utils.Resource
import com.besirkaraoglu.livechat.data.model.Users
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class UsersRepository(
    private val database: DatabaseReference
) {

    suspend fun insertUser(user: Users): Flow<Resource<Users>> =
        withContext(Dispatchers.IO){
            callbackFlow {
                trySend(Resource.Loading())
                val onCompleteListener = OnCompleteListener<Void>{ task ->
                    if (task.isSuccessful){
                        trySend(Resource.Success(user))
                    }else{
                        trySend(Resource.Error(task.exception?.message ?: "Insert failed!"))
                    }
                }
                val onCanceledListener = OnCanceledListener{
                    trySend(Resource.Error("Insert operation canceled!"))
                }
                if (user.uid == null)
                    Timber.e("Uid is null!")
                else {
                    val operation = database.child(user.uid).setValue(user)
                   operation.addOnCompleteListener(onCompleteListener)
                       .addOnCanceledListener(onCanceledListener)
                }
                awaitClose {  }
            }
    }



}