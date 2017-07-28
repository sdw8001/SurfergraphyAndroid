@file:JvmName("RealmUtils") // pretty name for utils class if called from
package com.surfergraphy.surfergraphy.utils

import com.surfergraphy.surfergraphy.base.data.ActionResponseDao
import com.surfergraphy.surfergraphy.login.data.AccessTokenDao
import com.surfergraphy.surfergraphy.login.data.AuthorizationAccountUserDao
import com.surfergraphy.surfergraphy.photos.data.PhotoDao
import com.surfergraphy.surfergraphy.photos.data.PhotoSaveHistoryDao
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

fun Realm.actionResponseModel(): ActionResponseDao = ActionResponseDao(this)
fun Realm.accessTokenModel(): AccessTokenDao = AccessTokenDao(this)
fun Realm.authorizationAccountUserDao(): AuthorizationAccountUserDao = AuthorizationAccountUserDao(this)
fun Realm.photoModel(): PhotoDao = PhotoDao(this)
fun Realm.photoSaveHistory(): PhotoSaveHistoryDao = PhotoSaveHistoryDao(this)

fun <T: RealmModel> RealmResults<T>.asLiveData() = LiveRealmData<T>(this)