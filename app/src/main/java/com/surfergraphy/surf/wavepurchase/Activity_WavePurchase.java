package com.surfergraphy.surf.wavepurchase;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.surfergraphy.surf.R;
import com.surfergraphy.surf.base.ActivityCode;
import com.surfergraphy.surf.base.BaseIntentKey;
import com.surfergraphy.surf.base.BaseType;
import com.surfergraphy.surf.base.activities.BaseActivity;
import com.surfergraphy.surf.base.helper.InAppBillingHelper;
import com.surfergraphy.surf.base.navigation.AppNavigationView;
import com.surfergraphy.surf.login.Activity_Login;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;

public class Activity_WavePurchase extends BaseActivity {

    private ViewModel_WavePurchase viewModel_wavePurchase;
    private Adapter_WavePurchase adapter_wavePurchase;
    private InAppBillingHelper inAppBillingHelper;
    private BaseType.OpenType openType = BaseType.OpenType.Navigation;

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

        openType = (BaseType.OpenType) getIntent().getSerializableExtra(BaseIntentKey.OpenType);
        if (openType == BaseType.OpenType.Navigation) {
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
        } else if (openType == BaseType.OpenType.Back) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        inAppBillingHelper = new InAppBillingHelper(this);
        viewModel_wavePurchase = ViewModelProviders.of(this).get(ViewModel_WavePurchase.class);
        viewModel_wavePurchase.setInAppBillingHelper(inAppBillingHelper);

        viewModel_wavePurchase.getLoginMemberLiveData().observe(this, accessToken -> {
            if (accessToken != null) {
                if (accessToken.Expired) {
                    Intent intent = new Intent(this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        viewModel_wavePurchase.getWavePurchasesLiveData().observe(this, wavePurchases -> {
            if (wavePurchases != null) {
                if (adapter_wavePurchase == null) {
                    adapter_wavePurchase = new Adapter_WavePurchase(this, wavePurchases, true, false, wavePurchase -> viewModel_wavePurchase.buyWavePurchase(wavePurchase));
                }
                realmRecyclerView_WavePurchase.setAdapter(adapter_wavePurchase);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(Activity_WavePurchase.class.getSimpleName(), "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (inAppBillingHelper == null || !inAppBillingHelper.onActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(Activity_WavePurchase.class.getSimpleName(), "onActivityResult handled by IABUtil.");
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
