package com.surfergraphy.surfergraphy.about;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.base.ActivityCode;
import com.surfergraphy.surfergraphy.base.activities.BaseActivity;
import com.surfergraphy.surfergraphy.base.navigation.AppNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_About extends BaseActivity {

    @BindView(R.id.nav_view)
    AppNavigationView appNavigationView;

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
    }

}
