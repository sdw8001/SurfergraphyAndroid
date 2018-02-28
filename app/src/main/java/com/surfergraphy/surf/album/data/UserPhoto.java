package com.surfergraphy.surf.album.data;

import com.google.gson.annotations.SerializedName;
import com.surfergraphy.surf.photos.data.Photo;
import com.surfergraphy.surf.photos.data.PhotoBuyHistory;
import com.surfergraphy.surf.photos.data.PhotoSaveHistory;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class UserPhoto implements RealmModel{


    @SerializedName("Id")
    @PrimaryKey
    public int id;
    @SerializedName("UserId")
    public String userId;
    @SerializedName("PhotoId")
    public int photoId;
    @SerializedName("PhotoSaveHistoryId")
    public int photoSaveHistoryId;
    @SerializedName("PhotoBuyHistoryId")
    public int photoBuyHistoryId;
    @SerializedName("Deleted")
    public boolean deleted;

    public Photo photo;
    public PhotoSaveHistory photoSaveHistory;
    public PhotoBuyHistory photoBuyHistory;

    public UserPhoto() {}

    public UserPhoto(int id, String userId, int photoId, int photoSaveHistoryId, int photoBuyHistoryId, boolean deleted) {
        this.id = id;
        this.userId = userId;
        this.photoId = photoId;
        this.photoSaveHistoryId = photoSaveHistoryId;
        this.photoBuyHistoryId = photoBuyHistoryId;
        this.deleted = deleted;
    }
}
