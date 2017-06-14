@file:JvmName("RealmUtils") // pretty name for utils class if called from
package com.surfergraphy.surfergraphy.login.data.utils

import com.surfergraphy.surfergraphy.login.LiveRealmData
import com.surfergraphy.surfergraphy.login.data.AccessTokenDao
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

fun Realm.accessTokenModel(): AccessTokenDao = AccessTokenDao(this)

fun <T: RealmModel> RealmResults<T>.asLiveData() = LiveRealmData<T>(this)
