package com.surfergraphy.surfergraphy.iab;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.base.ActivityCode;
import com.surfergraphy.surfergraphy.base.activities.BaseActivity;
import com.surfergraphy.surfergraphy.base.navigation.AppNavigationView;
import com.surfergraphy.surfergraphy.iab.data.WavePurchase;
import com.surfergraphy.surfergraphy.login.Activity_Login;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;

public class Activity_WavePurchase extends BaseActivity {

    private ViewModel_WavePurchase viewModel_wavePurchase;
    private Adapter_WavePurchase adapter_wavePurchase;

    @BindView(R.id.nav_view)
    AppNavigationView appNavigationView;

    @BindView(R.id.rrv_recycler_view_wave_purchase)
    RealmRecyclerView realmRecyclerView_WavePurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_purchase);
        ButterKnife.bind(this);
        appNavigationView.setCurrentActivityCode(ActivityCode.ACTIVITY_WAVE_PURCHASE);

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


        viewModel_wavePurchase = ViewModelProviders.of(this).get(ViewModel_WavePurchase.class);

        viewModel_wavePurchase.getAccessToken().observe(this, accessToken -> {
            if (accessToken != null) {
                if (accessToken.expired) {
                    Intent intent = new Intent(this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        viewModel_wavePurchase.getWavePurchases().observe(this, wavePurchases -> {
            if (wavePurchases != null) {
                if (adapter_wavePurchase == null) {
                    adapter_wavePurchase = new Adapter_WavePurchase(this, wavePurchases, true, false, wavePurchase -> {
                        String message = String.valueOf(wavePurchase.waveCount) + " Wave를\n" + String.format("%,d", wavePurchase.wavePrice) + "원에 구매하시겠습니까?";
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Wave 구매하기")
                                .setMessage(message)
                                .setCancelable(true)
                                .setPositiveButton("구매",(dialog, which) -> viewModel_wavePurchase.buyWavePurchase(wavePurchase))
                                .setNegativeButton("취소", (dialog, which) -> dialog.cancel());
                        AlertDialog buyDialog = builder.create();
                        buyDialog.show();
                    });
                }
                realmRecyclerView_WavePurchase.setAdapter(adapter_wavePurchase);
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
}
