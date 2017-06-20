@file:JvmName("RealmUtils") // pretty name for utils class if called from
package com.surfergraphy.surfergraphy.utils

import com.surfergraphy.surfergraphy.login.data.AccessTokenDao
import com.surfergraphy.surfergraphy.photos.data.PhotoDao
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

fun Realm.accessTokenModel(): AccessTokenDao = AccessTokenDao(this)
fun Realm.photoModel(): PhotoDao = PhotoDao(this)

fun <T: RealmModel> RealmResults<T>.asLiveData() = LiveRealmData<T>(this)