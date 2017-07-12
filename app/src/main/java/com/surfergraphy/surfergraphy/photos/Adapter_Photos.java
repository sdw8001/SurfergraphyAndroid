package com.surfergraphy.surfergraphy.photos;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.login.Activity_Login;
import com.surfergraphy.surfergraphy.photos.data.Photo;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class Adapter_Photos extends RealmBasedRecyclerViewAdapter<Photo, Adapter_Photos.ViewHolder> {

    public Adapter_Photos(Context context, RealmResults<Photo> realmResults, boolean automaticUpdate, boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);

    }

    public class ViewHolder extends RealmViewHolder {

        public ImageView imageView_Photo;
        public TextView textView_Watermark;

        public ViewHolder(ViewGroup container) {
            super(container);
            imageView_Photo = (ImageView) container.findViewById(R.id.image_view_photo);
            textView_Watermark = (TextView) container.findViewById(R.id.text_view_watermark);
            textView_Watermark.bringToFront();
        }
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.item_photos_recyclerview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder((ViewGroup) view);
        return viewHolder;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final Photo photo = realmResults.get(position);

        Glide.with(getContext()).load(photo.url).into(viewHolder.imageView_Photo);
        viewHolder.textView_Watermark.setVisibility(View.VISIBLE);
        viewHolder.imageView_Photo.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Activity_PhotoDetail.class);
            intent.putExtra("photo_id", photo.id);
            getContext().startActivity(intent);
        });
    }
}
