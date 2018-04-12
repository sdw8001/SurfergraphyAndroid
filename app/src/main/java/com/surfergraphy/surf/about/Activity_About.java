package com.surfergraphy.surf.about;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.surfergraphy.surf.R;
import com.surfergraphy.surf.base.ActivityCode;
import com.surfergraphy.surf.base.activities.BaseActivity;
import com.surfergraphy.surf.base.navigation.AppNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_About extends BaseActivity {

    @BindView(R.id.nav_view)
    AppNavigationView appNavigationView;

    @BindView(R.id.logo_bottom)
    ImageView logoBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        appNavigationView.setCurrentActivityCode(ActivityCode.ACTIVITY_ABOUT);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                viewModelLogin.syncAuthorizationAccountUser();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Glide.with(Activity_About.this).load(R.drawable.surfergraphy_agency).into(logoBottom);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
