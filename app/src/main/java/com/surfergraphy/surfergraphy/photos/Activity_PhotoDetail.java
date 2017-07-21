package com.surfergraphy.surfergraphy.photos;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.base.activities.BaseActivity;
import com.surfergraphy.surfergraphy.base.activities.BaseLifecycleActivity;
import com.surfergraphy.surfergraphy.login.Activity_Login;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_PhotoDetail extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ViewModel_PhotoDetail viewModel_photoDetail;

    @BindView(R.id.image_view_photo)
    ImageView imageView_Photo;
    @BindView(R.id.text_view_watermark)
    TextView textView_Watermark;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(this);
        textView_Watermark.bringToFront();

        viewModel_photoDetail = ViewModelProviders.of(this).get(ViewModel_PhotoDetail.class);
        viewModel_photoDetail.getAccessToken().observe(this, accessToken -> {
            if (accessToken != null) {
                if (accessToken.expired) {
                    Intent intent = new Intent(this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        viewModel_photoDetail.getPhoto(getIntent().getIntExtra("photo_id", 0)).observe(this, photo -> {
            Glide.with(this).load(photo.url).into(imageView_Photo);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
