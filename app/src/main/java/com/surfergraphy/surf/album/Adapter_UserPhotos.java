package com.surfergraphy.surf.album;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.surfergraphy.surf.R;
import com.surfergraphy.surf.album.data.UserPhoto;
import com.surfergraphy.surf.photos.Activity_PhotoDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class Adapter_UserPhotos extends RealmBasedRecyclerViewAdapter<UserPhoto, Adapter_UserPhotos.ViewHolder> {

    public ArrayList<UserPhoto> selectedPhotoIds;

    public Adapter_UserPhotos(Context context, RealmResults<UserPhoto> realmResults, boolean automaticUpdate, boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        selectedPhotoIds = new ArrayList<>();
    }

    public class ViewHolder extends RealmViewHolder {

        @BindView(R.id.image_view_photo)
        ImageView imageView_Photo;

        @BindView(R.id.select_box)
        CheckBox checkBox_Photo;

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
        if (selectedPhotoIds != null)
            selectedPhotoIds.clear();

        if (userPhoto.photo != null) {
            Glide.with(getContext()).load(userPhoto.photo.url).apply(RequestOptions.skipMemoryCacheOf(false)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).thumbnail(0.1f).into(viewHolder.imageView_Photo);
            viewHolder.checkBox_Photo.setVisibility(userPhoto.selectable ? View.VISIBLE : View.GONE);
            viewHolder.imageView_Photo.setOnClickListener(v -> {
                if (userPhoto.selectable) {
                    viewHolder.checkBox_Photo.toggle();
                } else {
                    Intent intent = new Intent(getContext(), Activity_PhotoDetail.class);
                    intent.putExtra("photo_id", userPhoto.photoId);
                    getContext().startActivity(intent);
                }
            });
            viewHolder.checkBox_Photo.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked)
                    selectedPhotoIds.add(userPhoto);
                else
                    selectedPhotoIds.remove(userPhoto);
            });
        }
    }
}
