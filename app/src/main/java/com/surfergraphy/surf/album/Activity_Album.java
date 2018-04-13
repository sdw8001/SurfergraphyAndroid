package com.surfergraphy.surf.album;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.surfergraphy.surf.R;
import com.surfergraphy.surf.base.ActivityCode;
import com.surfergraphy.surf.base.activities.BaseActivity;
import com.surfergraphy.surf.base.navigation.AppNavigationView;
import com.surfergraphy.surf.login.Activity_Login;
import com.surfergraphy.surf.utils.CommonTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;

public class Activity_Album extends BaseActivity {

    private ViewModel_UserPhoto viewModel_userPhoto;
    private Adapter_UserPhotos adapter_userPhotos;

    @BindView(R.id.nav_view)
    AppNavigationView appNavigationView;

    @BindView(R.id.select)
    ViewGroup select;
    @BindView(R.id.download)
    ViewGroup download;
    @BindView(R.id.delete)
    ViewGroup delete;

    @BindView(R.id.rrv_recycler_view_photos)
    RealmRecyclerView realmRecyclerView_Photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        appNavigationView.setCurrentActivityCode(ActivityCode.ACTIVITY_ALBUM);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        viewModel_userPhoto = ViewModelProviders.of(this).get(ViewModel_UserPhoto.class);

        viewModel_userPhoto.getLoginMemberLiveData().observe(this, accessToken -> {
            if (accessToken != null) {
                if (accessToken.Expired) {
                    Intent intent = new Intent(this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        viewModel_userPhoto.syncDataUserPhotos();
        viewModel_userPhoto.getUserPhotos().observe(this, photos -> {
            if (photos != null) {
                if (adapter_userPhotos == null) {
                    adapter_userPhotos = new Adapter_UserPhotos(this, photos, true, false);
                }
                realmRecyclerView_Photos.setAdapter(adapter_userPhotos);
            }
        });

        select.setOnClickListener(v -> {
            viewModel_userPhoto.toggleSelectableUserPhoto(viewModelLogin.getLoginMember().Id);
            adapter_userPhotos.notifyDataSetChanged();
        });
        download.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Album.this);
            builder.setMessage("Would you like to download selected pictures?").setPositiveButton("Yes", downloadDialogClickListener).setNegativeButton("No", downloadDialogClickListener).show();
        });
        delete.setVisibility(View.GONE); // TODO 추후 작업예정
        delete.setOnClickListener(v -> {

        });

    }

    private DialogInterface.OnClickListener downloadDialogClickListener = (dialogInterface, i) -> {
        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:
                // Yes
                for (Integer id : adapter_userPhotos.selectedPhotoIds) {
                    CommonTask.savePicture(this, viewModel_userPhoto.getUserPhotoUrl(id), false);
                }
                // TODO 콜백처리 아직안됨
                if (adapter_userPhotos.selectedPhotoIds.size() > 0) {
                    this.realmRecyclerView_Photos.postDelayed(() -> {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "선택된 사진이 갤러리에 저장되었습니다.", Snackbar.LENGTH_SHORT).show();
                    }, 2000);
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                // No
                break;
        }
    };

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
