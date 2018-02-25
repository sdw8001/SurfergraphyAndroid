package com.surfergraphy.surfergraphy.like.data;

import com.google.gson.annotations.SerializedName;
import com.surfergraphy.surfergraphy.photos.data.Photo;
import com.surfergraphy.surfergraphy.photos.data.PhotoBuyHistory;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class LikePhoto implements RealmModel {

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
    public Photo photo;
    public PhotoBuyHistory photoBuyHistory;

    public LikePhoto() {
    }

    public LikePhoto(int id, String userId, int photoId, int photoBuyHistoryId, boolean deleted) {
        this.id = id;
        this.userId = userId;
        this.photoId = photoId;
        this.photoBuyHistoryId = photoBuyHistoryId;
        this.deleted = deleted;
    }
}
