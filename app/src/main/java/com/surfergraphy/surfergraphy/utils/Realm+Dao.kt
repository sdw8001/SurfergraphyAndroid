@file:JvmName("RealmUtils") // pretty name for utils class if called from
package com.surfergraphy.surfergraphy.utils

import com.surfergraphy.surfergraphy.album.data.UserPhotoDao
import com.surfergraphy.surfergraphy.base.data.ActionResponseDao
import com.surfergraphy.surfergraphy.like.data.LikePhotoDao
import com.surfergraphy.surfergraphy.wavepurchase.data.WavePurchaseDao
import com.surfergraphy.surfergraphy.login.data.AccessTokenDao
import com.surfergraphy.surfergraphy.login.data.AuthorizationAccountUserDao
import com.surfergraphy.surfergraphy.photos.data.*
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

fun Realm.actionResponseModel(): ActionResponseDao = ActionResponseDao(this)
fun Realm.accessTokenModel(): AccessTokenDao = AccessTokenDao(this)
fun Realm.authorizationAccountUserDao(): AuthorizationAccountUserDao = AuthorizationAccountUserDao(this)
fun Realm.photoModel(): PhotoDao = PhotoDao(this)
fun Realm.photographerModel(): PhotographerDao = PhotographerDao(this)
fun Realm.userPhotoModel(): UserPhotoDao = UserPhotoDao(this)
fun Realm.photoSaveHistoryModel(): PhotoSaveHistoryDao = PhotoSaveHistoryDao(this)
fun Realm.likePhotoModel(): LikePhotoDao = LikePhotoDao(this)
fun Realm.photoBuyHistoryModel(): PhotoBuyHistoryDao = PhotoBuyHistoryDao(this)
fun Realm.wavePurchaseModel(): WavePurchaseDao = WavePurchaseDao(this)

fun <T: RealmModel> RealmResults<T>.asLiveData() = LiveRealmData<T>(this)