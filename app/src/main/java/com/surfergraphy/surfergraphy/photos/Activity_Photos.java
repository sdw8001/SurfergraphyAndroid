package com.surfergraphy.surfergraphy.photos;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.base.activities.BaseActivity;
import com.surfergraphy.surfergraphy.login.Activity_Login;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;

public class Activity_Photos extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewModel_Photo viewModelPhoto;
    private Adapter_Photos adapterPhotos;

    @BindView(R.id.rrv_recycler_view_photos)
    RealmRecyclerView realmRecyclerView_Photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        viewModelPhoto = ViewModelProviders.of(this).get(ViewModel_Photo.class);

        viewModelPhoto.getAccessToken().observe(this, accessToken -> {
            if (accessToken != null) {
                if (accessToken.expired) {
                    Intent intent = new Intent(this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        viewModelPhoto.getViewInfo_Photo().observe(this, viewInfo_photos -> {
            if (viewInfo_photos != null && viewInfo_photos.size() == 1) {
                String place = viewInfo_photos.get(0).place;
                if (TextUtils.isEmpty(place)) {
                    this.setTitle("사진 > " + "전체");
                    viewModelPhoto.dataSyncPhotos();
                } else {
                    this.setTitle("사진 > " + place);
                    viewModelPhoto.dataSyncPlacePhotos(place);
                }
            }
        });
        viewModelPhoto.getPhotos().observe(this, photos -> {
            if (photos != null) {
                if (adapterPhotos == null) {
                    adapterPhotos = new Adapter_Photos(this, photos, true, false);
                }
                realmRecyclerView_Photos.setAdapter(adapterPhotos);
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
