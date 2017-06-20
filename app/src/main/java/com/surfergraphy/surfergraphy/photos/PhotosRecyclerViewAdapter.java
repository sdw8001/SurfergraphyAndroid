package com.surfergraphy.surfergraphy.photos;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.photos.data.Photo;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class PhotosRecyclerViewAdapter extends RealmBasedRecyclerViewAdapter<Photo, PhotosRecyclerViewAdapter.ViewHolder> {

    public PhotosRecyclerViewAdapter(Context context, RealmResults<Photo> realmResults, boolean automaticUpdate, boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);

    }

    public class ViewHolder extends RealmViewHolder {

        public ImageView imageView_Photo;

        public ViewHolder(LinearLayout container) {
            super(container);
            imageView_Photo = (ImageView) container.findViewById(R.id.imageView_Photo);
        }
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.item_photos_recyclerview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder((LinearLayout) view);
        return viewHolder;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final Photo photo = realmResults.get(position);

        Glide.with(getContext()).load(photo.url).into(viewHolder.imageView_Photo);
    }
}
