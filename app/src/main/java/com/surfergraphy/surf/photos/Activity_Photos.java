package com.surfergraphy.surf.photos;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surfergraphy.surf.R;
import com.surfergraphy.surf.base.ActivityCode;
import com.surfergraphy.surf.base.BaseIntentKey;
import com.surfergraphy.surf.base.BaseType;
import com.surfergraphy.surf.base.activities.BaseActivity;
import com.surfergraphy.surf.base.navigation.AppNavigationView;
import com.surfergraphy.surf.login.Activity_Login;
import com.surfergraphy.surf.photos.data.PhotoDate;

import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;

public class Activity_Photos extends BaseActivity {

    private ViewModel_Photo viewModelPhoto;
    private Adapter_Photos adapterPhotos;
    private BaseType.LocationType locationType;
    private BaseType.OpenType openType = BaseType.OpenType.Navigation;

    private String currentDate;

    @BindView(R.id.nav_view)
    AppNavigationView appNavigationView;

    @BindView(R.id.rrv_recycler_view_photos)
    RealmRecyclerView realmRecyclerView_Photos;

    @BindView(R.id.layout_dates)
    LinearLayout layoutDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        ButterKnife.bind(this);
        appNavigationView.setCurrentActivityCode(ActivityCode.ACTIVITY_PHOTOS);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                viewModelLogin.syncAuthorizationAccountUser();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        openType = (BaseType.OpenType) getIntent().getSerializableExtra(BaseIntentKey.OpenType);
        if (openType == BaseType.OpenType.OpenNavigation) {
            drawer.openDrawer(GravityCompat.START);
        }

        viewModelPhoto = ViewModelProviders.of(this).get(ViewModel_Photo.class);
        viewModelPhoto.getLoginMemberLiveData().observe(this, accessToken -> {
            if (accessToken != null) {
                if (accessToken.Expired) {
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
                    viewModelPhoto.dataSyncDates();
                } else {
                    this.setTitle(locationType.getName());
                    viewModelPhoto.dataSyncDatesFromPlace(locationType);
                }
            }
        });
        viewModelPhoto.getDates().observe(this, photoDates -> {
            layoutDates.removeAllViews();
            if (photoDates != null && photoDates.size() > 0) {
                for (PhotoDate photoDate : photoDates) {
                    TextView dateButton = (TextView) getLayoutInflater().inflate(R.layout.item_date, layoutDates, false);
                    dateButton.setText(photoDate.date.substring(0, 4) + "." + photoDate.date.substring(4, 6) + "." + photoDate.date.substring(6, 8));
                    dateButton.setOnClickListener(v -> {
                        selectDate(((TextView) v).getText().toString().replace(".", ""));
                        for (int i = 0; i < layoutDates.getChildCount(); i++) {
                            TextView view = (TextView) layoutDates.getChildAt(i);
                            view.setSelected(view == v);
                        }
                    });
                    dateButton.setSelected(layoutDates.getChildCount() == 0);
                    layoutDates.addView(dateButton);
                }

                selectDate(photoDates.get(0).date);
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
                    appNavigationView.selectMenuLocation(BaseType.LocationType.Korea_EastCoast);
                    break;
                case Korea_SouthCoast:
                    appNavigationView.selectMenuLocation(BaseType.LocationType.Korea_SouthCoast);
                    break;
                case Korea_WestCoast:
                    appNavigationView.selectMenuLocation(BaseType.LocationType.Korea_WestCoast);
                    break;
                case Korea_JejuIsland:
                    appNavigationView.selectMenuLocation(BaseType.LocationType.Korea_JejuIsland);
                    break;
            }
        }
    }

    private void selectDate(final String date) {
        this.currentDate = date;
        viewModelPhoto.deletePhotos();
        if (locationType == null) {
            this.setTitle("Photos");
            viewModelPhoto.dataSyncPhotos(currentDate);
        } else {
            this.setTitle(locationType.getName());
            viewModelPhoto.dataSyncPlacePhotos(locationType, currentDate);
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

        return super.onOptionsItemSelected(item);
    }
}
