package com.surfergraphy.surfergraphy.album;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.album.data.UserPhoto;
import com.surfergraphy.surfergraphy.photos.Activity_PhotoDetail;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class Adapter_UserPhotos extends RealmBasedRecyclerViewAdapter<UserPhoto, Adapter_UserPhotos.ViewHolder> {

    public Adapter_UserPhotos(Context context, RealmResults<UserPhoto> realmResults, boolean automaticUpdate, boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);

    }

    public class ViewHolder extends RealmViewHolder {

        @BindView(R.id.image_view_photo)
        ImageView imageView_Photo;

        public ViewHolder(ViewGroup container) {
            super(container);
            ButterKnife.bind(this, container);
        }
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.item_user_photos_recyclerview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder((ViewGroup) view);
        return viewHolder;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final UserPhoto userPhoto = realmResults.get(position);
        if (userPhoto.photo != null) {
            Glide.with(getContext()).load(userPhoto.photo.url).thumbnail(0.1f).into(viewHolder.imageView_Photo);
            viewHolder.imageView_Photo.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), Activity_PhotoDetail.class);
                intent.putExtra("photo_id", userPhoto.photoId);
                getContext().startActivity(intent);
            });
        }
    }
}
