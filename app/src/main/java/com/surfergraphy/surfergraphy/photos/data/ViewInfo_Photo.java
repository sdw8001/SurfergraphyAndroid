package com.surfergraphy.surfergraphy.photos.data;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ViewInfo_Photo implements RealmModel {

    public String place;

    public ViewInfo_Photo() {
    }

    public ViewInfo_Photo(String place) {
        this.place = place;
    }
}
