package com.surfergraphy.surfergraphy.photos;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.base.ActivityCode;
import com.surfergraphy.surfergraphy.base.BaseType;
import com.surfergraphy.surfergraphy.base.activities.BaseActivity;
import com.surfergraphy.surfergraphy.base.navigation.AppNavigationView;
import com.surfergraphy.surfergraphy.login.Activity_Login;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;

public class Activity_Photos extends BaseActivity {

    private ViewModel_Photo viewModelPhoto;
    private Adapter_Photos adapterPhotos;
    private BaseType.LocationType locationType;

    @BindView(R.id.nav_view)
    AppNavigationView appNavigationView;

    @BindView(R.id.rrv_recycler_view_photos)
    RealmRecyclerView realmRecyclerView_Photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        ButterKnife.bind(this);
        appNavigationView.setCurrentActivityCode(ActivityCode.ACTIVITY_PHOTOS);

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
                this.locationType = BaseType.LocationType.findLocationType(viewInfo_photos.get(0).place);
                if (locationType == null) {
                    this.setTitle("Photos");
                    viewModelPhoto.dataSyncPhotos();
                } else {
                    this.setTitle(locationType.getName());
                    viewModelPhoto.dataSyncPlacePhotos(locationType);
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
        this.locationType = (BaseType.LocationType) getIntent().getSerializableExtra("place");
        if (locationType != null) {
            switch (locationType) {
                case Korea_EastCoast:
                    appNavigationView.onNavigationItemSelected(appNavigationView.getMenu().findItem(R.id.nav_korea_east_coast));
                    break;
                case Korea_SouthCoast:
                    appNavigationView.onNavigationItemSelected(appNavigationView.getMenu().findItem(R.id.nav_korea_south_coast));
                    break;
                case Korea_WestCoast:
                    appNavigationView.onNavigationItemSelected(appNavigationView.getMenu().findItem(R.id.nav_korea_west_coast));
                    break;
                case Korea_JejuIsland:
                    appNavigationView.onNavigationItemSelected(appNavigationView.getMenu().findItem(R.id.nav_korea_jeju_island));
                    break;
            }
        }
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
}
