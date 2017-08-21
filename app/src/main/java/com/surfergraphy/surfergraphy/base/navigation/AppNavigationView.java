package com.surfergraphy.surfergraphy.base.navigation;

import android.app.Activity;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.album.Activity_Album;
import com.surfergraphy.surfergraphy.base.ActivityCode;
import com.surfergraphy.surfergraphy.base.activities.BaseActivity;
import com.surfergraphy.surfergraphy.base.viewmodel.BaseViewModel;
import com.surfergraphy.surfergraphy.iab.Activity_WavePurchase;
import com.surfergraphy.surfergraphy.login.ViewModel_Login;
import com.surfergraphy.surfergraphy.photos.Activity_Photos;
import com.surfergraphy.surfergraphy.photos.ViewModel_Photo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppNavigationView extends NavigationView implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private int currentActivityCode;
    private ViewModel_Login viewModelLogin;
    private ViewModel_Photo viewModelPhoto;
    HeaderViewHolder headerViewHolder;

    public AppNavigationView(Context context) {
        this(context, null);
    }

    public AppNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        viewModelPhoto = ViewModelProviders.of((BaseActivity) context).get(ViewModel_Photo.class);
        viewModelLogin = ViewModelProviders.of((BaseActivity) context).get(ViewModel_Login.class);
        View header = getHeaderView(0);
        headerViewHolder = new HeaderViewHolder(header);
        setNavigationItemSelectedListener(this);

        viewModelLogin.getAccountUserLiveData().observe((LifecycleActivity) context, authorizationAccountUser -> {
            if (authorizationAccountUser != null) {
                headerViewHolder.nickName.setText(authorizationAccountUser.nickName);
                headerViewHolder.email.setText(authorizationAccountUser.email);
                headerViewHolder.wave.setText(String.valueOf(authorizationAccountUser.wave));
            }
        });
    }

    @Override
    public Menu getMenu() {
        return super.getMenu();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_info) {
            // Handle the camera action
        } else if (id == R.id.nav_my_wave) {
            if (currentActivityCode != ActivityCode.ACTIVITY_WAVE_PURCHASE) {
                Intent intent = new Intent(context, Activity_WavePurchase.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        }  else if (id == R.id.nav_my_gallery) {
            if (currentActivityCode != ActivityCode.ACTIVITY_ALBUM) {
                Intent intent = new Intent(context, Activity_Album.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        } else if (id == R.id.nav_my_cart) {

        } else if (id == R.id.nav_yangyang) {
            if (currentActivityCode != ActivityCode.ACTIVITY_PHOTOS) {
                Intent intent = new Intent(context, Activity_Photos.class);
                intent.putExtra("place", "양양");
                context.startActivity(intent);
                ((Activity) context).finish();
                return true;
            }
            viewModelPhoto.setPlace("양양");
            viewModelPhoto.deletePhotos();
            viewModelPhoto.dataSyncPlacePhotos("양양");
        } else if (id == R.id.nav_busan) {
            if (currentActivityCode != ActivityCode.ACTIVITY_PHOTOS) {
                Intent intent = new Intent(context, Activity_Photos.class);
                intent.putExtra("place", "부산");
                context.startActivity(intent);
                ((Activity) context).finish();
                return true;
            }
            viewModelPhoto.setPlace("부산");
            viewModelPhoto.deletePhotos();
            viewModelPhoto.dataSyncPlacePhotos("부산");
        } else if (id == R.id.nav_jeju) {
            if (currentActivityCode != ActivityCode.ACTIVITY_PHOTOS) {
                Intent intent = new Intent(context, Activity_Photos.class);
                intent.putExtra("place", "제주");
                context.startActivity(intent);
                ((Activity) context).finish();
                return true;
            }
            viewModelPhoto.setPlace("제주");
            viewModelPhoto.deletePhotos();
            viewModelPhoto.dataSyncPlacePhotos("제주");
        } else if (id == R.id.nav_logout) {
            viewModelLogin.logoutAccount();
        }

        DrawerLayout drawer = (DrawerLayout) ((BaseActivity) context).findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public int getCurrentActivityCode() {
        return currentActivityCode;
    }

    public void setCurrentActivityCode(int currentActivityCode) {
        this.currentActivityCode = currentActivityCode;
    }

    protected static class HeaderViewHolder {

        @BindView(R.id.text_view_nick_name)
        TextView nickName;
        @BindView(R.id.text_view_user_email)
        TextView email;
        @BindView(R.id.text_view_wave)
        TextView wave;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
