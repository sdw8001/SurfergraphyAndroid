package com.surfergraphy.surfergraphy.photos;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.base.ActionCode;
import com.surfergraphy.surfergraphy.base.activities.BaseActivity;
import com.surfergraphy.surfergraphy.login.Activity_Login;
import com.surfergraphy.surfergraphy.photos.data.Photo;
import com.surfergraphy.surfergraphy.utils.ResponseAction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_PhotoDetail extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ViewModel_PhotoDetail viewModel_photoDetail;

    @BindView(R.id.image_view_photo)
    ImageView imageView_Photo;
    @BindView(R.id.text_view_watermark)
    TextView textView_Watermark;
    @BindView(R.id.text_view_price)
    TextView textView_WavePrice;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.button_save)
    Button button_Save;
    @BindView(R.id.button_buy)
    Button button_Buy;

    private Photo photo;

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
            this.photo = photo;
            Glide.with(this).load(photo.url).into(imageView_Photo);
            textView_WavePrice.setText(String.valueOf(photo.wave));
        });

        viewModel_photoDetail.getActionResponse(ActionCode.ACTION_PHOTO_DETAIL_SAVE).observe(this, actionResponse -> {
            if (actionResponse != null) {
                switch (actionResponse.getResultCode()) {
                    case ResponseAction.HTTP_201_OK_CREATED:
                        Toast.makeText(this, actionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    case ResponseAction.HTTP_400_BAD_REQUEST:
                        Toast.makeText(this, actionResponse.getDetailMessages(), Toast.LENGTH_SHORT).show();
                        break;
                }
                viewModel_photoDetail.expiredActionToken(actionResponse.getActionCode());
            }
        });
        button_Save.setOnClickListener(v -> viewModel_photoDetail.savePhoto(ActionCode.ACTION_PHOTO_DETAIL_SAVE, viewModel_photoDetail.getAccountUser().id, photo.id));
        button_Buy.setOnClickListener(v -> viewModel_photoDetail.buyPhoto(ActionCode.ACTION_PHOTO_DETAIL_BUY, viewModel_photoDetail.getAccountUser().id, photo.id));
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
