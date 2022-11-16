package com.besirkaraoglu.livechat.data.repository

import com.besirkaraoglu.livechat.core.utils.Resource
import com.besirkaraoglu.livechat.data.model.LocationRecord
import com.besirkaraoglu.livechat.data.model.Users
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class UsersRepository(
    private val usersDatabase: DatabaseReference,
    private val locationRecordsDatabase: DatabaseReference
) {

    suspend fun insertUser(user: Users): Flow<Resource<Users>> =
        withContext(Dispatchers.IO) {
            callbackFlow {
                trySend(Resource.Loading())
                val onCompleteListener = OnCompleteListener<Void> { task ->
                    if (task.isSuccessful) {
                        trySend(Resource.Success(user))
                    } else {
                        trySend(Resource.Error(task.exception?.message ?: "Insert failed!"))
                    }
                }
                val onCanceledListener = OnCanceledListener {
                    trySend(Resource.Error("Insert operation canceled!"))
                }
                if (user.uid == null)
                    Timber.e("Uid is null!")
                else {
                    val operation = usersDatabase.child(user.uid).setValue(user)
                    operation.addOnCompleteListener(onCompleteListener)
                        .addOnCanceledListener(onCanceledListener)
                }
                awaitClose { }
            }
        }

    suspend fun queryUser(uid: String): Flow<Resource<Users>> =
        withContext(Dispatchers.IO) {
            callbackFlow {
                trySend(Resource.Loading())
                val onCompleteListener = OnCompleteListener<DataSnapshot> { task ->
                    if (task.isSuccessful) {
                        Timber.tag("QueryUser").d(task.result.toString())
                        if (task.result != null)
                            trySend(Resource.Success(task.result.getValue(Users::class.java)!!))
                        else
                            trySend(Resource.Empty())
                    } else {
                        trySend(Resource.Error(task.exception?.message ?: "Query failed!"))
                    }
                }
                val onCanceledListener = OnCanceledListener {
                    trySend(Resource.Error("Query operation canceled!"))
                }
                val operation = usersDatabase.child(uid).get()
                operation.addOnCompleteListener(onCompleteListener)
                    .addOnCanceledListener(onCanceledListener)

                awaitClose { }
            }
        }


    suspend fun upsertActiveUser(locationRecord: LocationRecord): Flow<Resource<LocationRecord>> =
        withContext(Dispatchers.IO) {
            callbackFlow {
                trySend(Resource.Loading())
                val onCompleteListener = OnCompleteListener<Void> { task ->
                    if (task.isSuccessful) {
                        trySend(Resource.Success(locationRecord))
                    } else {
                        trySend(Resource.Error(task.exception?.message ?: "Insert failed!"))
                    }
                }
                val onCanceledListener = OnCanceledListener {
                    trySend(Resource.Error("Insert operation canceled!"))
                }
                if (locationRecord.user!!.uid == null)
                    Timber.e("Uid is null!")
                else {
                    val operation =
                        locationRecordsDatabase.child(locationRecord.user.uid!!)
                            .setValue(locationRecord)
                    operation.addOnCompleteListener(onCompleteListener)
                        .addOnCanceledListener(onCanceledListener)
                }
                awaitClose { }
            }
        }

    suspend fun getLocationRecords(): Flow<Resource<Map<String, LocationRecord>>> =
        withContext(Dispatchers.IO) {
            callbackFlow {

            }
        }
}