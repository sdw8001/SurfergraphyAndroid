package com.surfergraphy.surfergraphy.base.navigation;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.base.activities.BaseActivity;
import com.surfergraphy.surfergraphy.login.ViewModel_Login;
import com.surfergraphy.surfergraphy.photos.ViewModel_Photo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppNavigationView extends NavigationView implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
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
        View header = getHeaderView(0);
        headerViewHolder = new HeaderViewHolder(header);
        setNavigationItemSelectedListener(this);


        viewModelPhoto = ViewModelProviders.of((BaseActivity) context).get(ViewModel_Photo.class);
        viewModelLogin = ViewModelProviders.of((BaseActivity) context).get(ViewModel_Login.class);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_info) {
            // Handle the camera action
        } else if (id == R.id.nav_my_gallery) {

        } else if (id == R.id.nav_my_cart) {

        } else if (id == R.id.nav_yangyang) {
            viewModelPhoto.setPlace("양양");
            viewModelPhoto.deletePhotos();
            viewModelPhoto.dataSyncPlacePhotos("양양");
        } else if (id == R.id.nav_busan) {
            viewModelPhoto.setPlace("부산");
            viewModelPhoto.deletePhotos();
            viewModelPhoto.dataSyncPlacePhotos("부산");
        } else if (id == R.id.nav_jeju) {
            viewModelPhoto.setPlace("제주");
            viewModelPhoto.deletePhotos();
            viewModelPhoto.dataSyncPlacePhotos("제주");
        } else if (id == R.id.nav_logout) {
            viewModelLogin.logoutAccount();
        }

        DrawerLayout drawer = (DrawerLayout) ((BaseActivity)context).findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected static class HeaderViewHolder {

        @BindView(R.id.text_view_nick_name)
        TextView nickName;
        @BindView(R.id.text_view_user_email)
        TextView email;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
