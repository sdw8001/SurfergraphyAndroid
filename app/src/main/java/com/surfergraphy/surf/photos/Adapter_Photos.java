package com.surfergraphy.surf.photos;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.surfergraphy.surf.R;
import com.surfergraphy.surf.photos.data.Photo;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class Adapter_Photos extends RealmBasedRecyclerViewAdapter<Photo, Adapter_Photos.ViewHolder> {

    public Adapter_Photos(Context context, RealmResults<Photo> realmResults, boolean automaticUpdate, boolean animateIdType) {
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
        View view = inflater.inflate(R.layout.item_photos_recyclerview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder((ViewGroup) view);
        return viewHolder;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final Photo photo = realmResults.get(position);

        Glide.with(getContext()).load(photo.url).apply(RequestOptions.skipMemoryCacheOf(false)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).thumbnail(0.1f).into(viewHolder.imageView_Photo);
        viewHolder.imageView_Photo.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Activity_PhotoDetail.class);
            intent.putExtra("photo_id", photo.id);
            getContext().startActivity(intent);
        });
    }
}
