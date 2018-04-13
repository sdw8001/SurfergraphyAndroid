package com.surfergraphy.surf.photos;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.surfergraphy.surf.R;
import com.surfergraphy.surf.base.ActionCode;
import com.surfergraphy.surf.base.BaseIntentKey;
import com.surfergraphy.surf.base.BaseType;
import com.surfergraphy.surf.base.activities.BaseActivity;
import com.surfergraphy.surf.like.ViewModel_LikePhoto;
import com.surfergraphy.surf.like.data.LikePhoto;
import com.surfergraphy.surf.login.Activity_Login;
import com.surfergraphy.surf.photos.data.Photo;
import com.surfergraphy.surf.utils.ResponseAction;
import com.surfergraphy.surf.wavepurchase.Activity_WavePurchase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_PhotoDetail extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ViewModel_PhotoDetail viewModel_photoDetail;
    private ViewModel_LikePhoto viewModel_likePhoto;

    @BindView(R.id.image_view_photo)
    ImageView imageView_Photo;
    @BindView(R.id.watermark)
    ImageView watermark;
    @BindView(R.id.quantity)
    TextView textView_Quantity;
    @BindView(R.id.text_view_price)
    TextView textView_WavePrice;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.favorite)
    View like;
    @BindView(R.id.button_save)
    View button_Save;
    @BindView(R.id.button_buy)
    View button_Buy;
    @BindView(R.id.button_buy_frame)
    View button_BuyFrame;

    @BindView(R.id.photographer_image)
    CircularImageView photographerImage;
    @BindView(R.id.photographer)
    TextView photographer;
    @BindView(R.id.created_date)
    TextView createdDate;
    @BindView(R.id.expiration_date)
    TextView expirationDate;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.dimensions)
    TextView dimensions;
    @BindView(R.id.resolution)
    TextView resolution;

    private Photo photo;
    private LikePhoto likePhoto;
    private String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        ButterKnife.bind(this);

        TedPermission.with(Activity_PhotoDetail.this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(this);
        watermark.bringToFront();

        viewModel_photoDetail = ViewModelProviders.of(this).get(ViewModel_PhotoDetail.class);
        viewModel_likePhoto = ViewModelProviders.of(this).get(ViewModel_LikePhoto.class);
        viewModel_photoDetail.getLoginMemberLiveData().observe(this, accessToken -> {
            if (accessToken != null) {
                if (accessToken.Expired) {
                    Intent intent = new Intent(this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        viewModel_photoDetail.getPhoto(getIntent().getIntExtra("photo_id", 0)).observe(this, photo -> {
            if (photo != null) {
                this.photo = photo;
                this.photoUrl = photo.url;
                Glide.with(this).load(photo.url).into(imageView_Photo);
                textView_Quantity.setText(String.valueOf(photo.totalCount));
                textView_WavePrice.setText(String.valueOf(photo.wave) + " wave");
                viewModel_likePhoto.syncDataLikePhotos();
                viewModel_likePhoto.getLikePhoto(viewModel_photoDetail.getLoginMember().Id, photo.id).observe(this, likePhoto1 -> {
                    this.likePhoto = likePhoto1;
                    like.setSelected(likePhoto1 != null);
                });

                viewModel_photoDetail.dataSyncPhotographer(photo.photographerId);
                viewModel_photoDetail.getPhotographer(photo.photographerId).observe(this, photographer1 -> {
                    if (photographer1 != null) {
                        Glide.with(Activity_PhotoDetail.this).load(photographer1.image).thumbnail(0.1f).into(photographerImage);
                        photographer.setText(photographer1.nickName);
                    }
                });
                createdDate.setText(photo.createdDate.substring(0, 10));
                expirationDate.setText(photo.expirationDate.substring(0, 10));
                location.setText(BaseType.LocationType.findLocationType(photo.place).getName());
                dimensions.setText(photo.dimensionWidth + " x " + photo.dimensionHeight + "px");
                resolution.setText(photo.resolution + "ppi");

                viewModel_photoDetail.getUserPhotoSaveHistory(viewModel_photoDetail.getLoginMember().Id, photo.id).observe(this, photoSaveHistory -> button_Save.setEnabled(photoSaveHistory == null));
                viewModel_photoDetail.getPhotoBuyHistory(viewModel_photoDetail.getLoginMember().Id, photo.id).observe(this, photoBuyHistory -> {
                    if (photoBuyHistory == null) {
                        watermark.setVisibility(View.VISIBLE);
                    } else {
                        watermark.setVisibility(View.GONE);
                    }
                });
                viewModel_photoDetail.getPhotoBuyHistories(photo.id).observe(this, photoBuyHistories -> {
                    textView_Quantity.setText(String.valueOf(photo.totalCount - photoBuyHistories.size()));
                    if (photoBuyHistories.size() < photo.totalCount) {
                        button_Buy.setEnabled(true);
                    } else {
                        button_Buy.setEnabled(false);
                    }
                });
            }
        });

        viewModel_photoDetail.getActionResponse(ActionCode.ACTION_CREATE_PHOTO_DETAIL_SAVE).observe(this, actionResponse -> {
            if (actionResponse != null && button_Save.isEnabled()) {
                switch (actionResponse.getResultCode()) {
                    case ResponseAction.HTTP_201_OK_CREATED:
                        savePicture(true);
                    case ResponseAction.HTTP_400_BAD_REQUEST:
                        Snackbar.make(getWindow().getDecorView().getRootView(), actionResponse.getMessage(), Snackbar.LENGTH_SHORT).show();
                        break;
                }
                viewModel_photoDetail.expiredActionToken(actionResponse.getActionCode());
            }
        });

        viewModel_photoDetail.getActionResponse(ActionCode.ACTION_CREATE_PHOTO_DETAIL_BUY).observe(this, actionResponse -> {
            if (actionResponse != null && button_Buy.isEnabled()) {
                switch (actionResponse.getResultCode()) {
                    case ResponseAction.HTTP_201_OK_CREATED:
                        savePicture(false);
                    case ResponseAction.HTTP_400_BAD_REQUEST:
                        Snackbar.make(getWindow().getDecorView().getRootView(), actionResponse.getMessage(), Snackbar.LENGTH_SHORT).show();
                        break;
                }
                viewModel_photoDetail.expiredActionToken(actionResponse.getActionCode());
            }
        });
        like.setOnClickListener(v -> {
            if (v.isSelected())
                viewModel_likePhoto.cancelLikePhoto(ActionCode.ACTION_LIKE_PHOTO_CANCEL, likePhoto.id);
            else
                viewModel_likePhoto.likePhoto(ActionCode.ACTION_LIKE_PHOTO, viewModel_photoDetail.getLoginMember().Id, photo.id);
            v.setSelected(!v.isSelected());
        });
        button_Save.setOnClickListener(v -> viewModel_photoDetail.savePhoto(ActionCode.ACTION_CREATE_PHOTO_DETAIL_SAVE, viewModel_photoDetail.getLoginMember().Id, photo.id));

        button_Buy.setOnClickListener(v -> {
            if (viewModelLogin.getLoginMember().Wave >= photo.wave) {
                // Wave 보유시 구매안내 팝업
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_PhotoDetail.this);
                builder.setMessage("Would you like to buy this picture?").setPositiveButton("Yes", buyDialogClickListener).setNegativeButton("No", buyDialogClickListener).show();
            } else {
                // Wave 미보유시 Wave 구매화면 이동안내 팝업
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_PhotoDetail.this);
                builder.setMessage("Not enough wave.\nWould you like to go charging?").setPositiveButton("Yes", goPurchaseDialogClickListener).setNegativeButton("No", goPurchaseDialogClickListener).show();
            }
        });
        button_BuyFrame.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://storefarm.naver.com/surfergraphy"))));
    }

    private void savePicture(boolean isIncludeWatermark) {
        if (TextUtils.isEmpty(photoUrl))
            return;
        new Thread() {
            public void run() {
                try {
                    URL imageUrl = new URL(photoUrl);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream()).copy(Bitmap.Config.ARGB_8888, true);
                    String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Surfergraphy/";
                    File directory = new File(extStorageDirectory);
                    if (!directory.exists())
                        directory.mkdirs();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
                    Date now = new Date();
                    File file = new File(extStorageDirectory, formatter.format(now) + ".png");
                    if (isIncludeWatermark) {
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setDither(true);
                        paint.setAlpha(160);
                        paint.setFilterBitmap(true);

                        Canvas canvas = new Canvas(bitmap);

                        Bitmap originWatermark = BitmapFactory.decodeResource(getResources(), R.drawable.surfergraphy_final_white);
                        int resizeWidth = (int) (bitmap.getWidth() * 0.8);
                        int width = originWatermark.getWidth();
                        int height = originWatermark.getHeight();
                        Bitmap resizedWatermark = Bitmap.createScaledBitmap(originWatermark, resizeWidth, height * resizeWidth / width, true);

                        Matrix matrix = new Matrix();
                        matrix.postTranslate(canvas.getWidth() / 2 - resizedWatermark.getWidth() / 2, canvas.getHeight() / 2 - resizedWatermark.getHeight() / 2);

                        canvas.drawBitmap(resizedWatermark, matrix, paint);
                    }
                    OutputStream outStream = null;
                    outStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                    outStream.flush();
                    outStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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

    private DialogInterface.OnClickListener buyDialogClickListener = (dialogInterface, i) -> {
        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:
                // Yes
                viewModel_photoDetail.buyPhoto(ActionCode.ACTION_CREATE_PHOTO_DETAIL_BUY, viewModel_photoDetail.getLoginMember().Id, photo.id);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                // No
                break;
        }
    };
    private DialogInterface.OnClickListener goPurchaseDialogClickListener = (dialogInterface, i) -> {
        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:
                // Yes
                Intent intent = new Intent(this, Activity_WavePurchase.class);
                intent.putExtra(BaseIntentKey.OpenType, BaseType.OpenType.Back);
                startActivity(intent);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                // No
                break;
        }
    };
}
