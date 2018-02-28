package com.surfergraphy.surf.photos.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class PhotoSaveHistory implements RealmModel {

    @SerializedName("Id")
    @PrimaryKey
    public int id;
    @SerializedName("UserId")
    public String userId;
    @SerializedName("PhotoId")
    public int photoId;
    @SerializedName("PhotoBuyHistoryId")
    public int photoBuyHistoryId;
    @SerializedName("Deleted")
    public boolean deleted;

    public PhotoSaveHistory() {}

    public PhotoSaveHistory(int id, String userId, int photoId, int photoBuyHistoryId, boolean deleted) {
        this.id = id;
        this.userId = userId;
        this.photoId = photoId;
        this.photoBuyHistoryId = photoBuyHistoryId;
        this.deleted = deleted;
    }
}
