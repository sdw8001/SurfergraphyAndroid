package com.surfergraphy.surf.base.navigation;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.surfergraphy.surf.R;
import com.surfergraphy.surf.about.Activity_About;
import com.surfergraphy.surf.album.Activity_Album;
import com.surfergraphy.surf.base.ActivityCode;
import com.surfergraphy.surf.base.BaseType;
import com.surfergraphy.surf.base.activities.BaseActivity;
import com.surfergraphy.surf.like.Activity_LikePhoto;
import com.surfergraphy.surf.login.ViewModel_Login;
import com.surfergraphy.surf.photos.Activity_Photos;
import com.surfergraphy.surf.photos.ViewModel_Photo;
import com.surfergraphy.surf.wavepurchase.Activity_WavePurchase;

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
        headerViewHolder = new HeaderViewHolder(context, header);
        setNavigationItemSelectedListener(this);
        setItemIconTintList(null);
        viewModelLogin.getLoginMemberLiveData().observe((BaseActivity) context, loginMember -> {
            if (loginMember != null) {
                headerViewHolder.nickName.setText(loginMember.Name);
                headerViewHolder.email.setText(loginMember.Email);
                headerViewHolder.wave.setText(String.valueOf(loginMember.Wave));
                Glide.with(context).load(loginMember.ImageUrl).thumbnail(0.1f).into(headerViewHolder.memberImage);
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

        if (id == R.id.nav_korea_east_coast) {
            selectMenuLocation(BaseType.LocationType.Korea_EastCoast);
        } else if (id == R.id.nav_korea_south_coast) {
            selectMenuLocation(BaseType.LocationType.Korea_SouthCoast);
        } else if (id == R.id.nav_korea_west_coast) {
            selectMenuLocation(BaseType.LocationType.Korea_WestCoast);
        } else if (id == R.id.nav_korea_jeju_island) {
            selectMenuLocation(BaseType.LocationType.Korea_JejuIsland);
        } else if (id == R.id.nav_japan) {
            selectMenuLocation(BaseType.LocationType.Japan);
        } else if (id == R.id.nav_china) {
            selectMenuLocation(BaseType.LocationType.China);
        } else if (id == R.id.nav_indonesia) {
            selectMenuLocation(BaseType.LocationType.Indonesia);
        } else if (id == R.id.nav_philippines) {
            selectMenuLocation(BaseType.LocationType.Philippines);
        } else if (id == R.id.nav_taiwan) {
            selectMenuLocation(BaseType.LocationType.Taiwan);
        } else if (id == R.id.nav_usa) {
            selectMenuLocation(BaseType.LocationType.Usa);
        } else if (id == R.id.nav_hawaii) {
            selectMenuLocation(BaseType.LocationType.Hawaii);
        } else if (id == R.id.nav_australia) {
            selectMenuLocation(BaseType.LocationType.Australia);
        } else if (id == R.id.nav_other_countries) {
            selectMenuLocation(BaseType.LocationType.OtherCountries);
        } else if (id == R.id.nav_logout) {
            viewModelLogin.logoutAccount();
            DrawerLayout drawer = (DrawerLayout) ((BaseActivity) context).findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void selectMenuLocation(BaseType.LocationType locationType) {
        if (currentActivityCode != ActivityCode.ACTIVITY_PHOTOS) {
            Intent intent = new Intent(context, Activity_Photos.class);
            intent.putExtra("place", locationType);
            context.startActivity(intent);
            ((Activity) context).finish();
            return;
        }
        viewModelPhoto.setPlace(locationType);
        viewModelPhoto.deletePhotos();
        viewModelPhoto.dataSyncPlacePhotos(locationType);
        DrawerLayout drawer = (DrawerLayout) ((BaseActivity) context).findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void setCurrentActivityCode(int currentActivityCode) {
        this.currentActivityCode = currentActivityCode;
    }

    protected static class HeaderViewHolder {

        @BindView(R.id.logo_site)
        ImageView logoSite;

        @BindView(R.id.text_view_nick_name)
        TextView nickName;
        @BindView(R.id.text_view_user_email)
        TextView email;
        @BindView(R.id.text_view_wave)
        TextView wave;
        @BindView(R.id.member_profile_image)
        CircularImageView memberImage;

        @BindView(R.id.nav_menu_top_collection)
        TextView collection;
        @BindView(R.id.nav_menu_top_downloaded)
        TextView downloaded;
        @BindView(R.id.nav_menu_top_buy_wave)
        TextView buyWave;
        @BindView(R.id.nav_menu_top_about)
        TextView about;

        HeaderViewHolder(Context context, View view) {
            ButterKnife.bind(this, view);

            Glide.with(context).load(R.drawable.member_photo).thumbnail(0.1f).into(memberImage);

            // 로고 클릭 - 웹사이트 이동
            logoSite.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.surfergraphy.com"));
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.e("BannerNotEnableUrl", e.getMessage());
                }
            });

            // 컬랙션 클릭
            collection.setOnClickListener(v -> {
                Intent intent = new Intent(context, Activity_LikePhoto.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            });

            // 보관함 클릭
            downloaded.setOnClickListener(v -> {
                Intent intent = new Intent(context, Activity_Album.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            });

            // 웨이브구매 클릭
            buyWave.setOnClickListener(v -> {
                Intent intent = new Intent(context, Activity_WavePurchase.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            });

            // 어바웃 클릭
            about.setOnClickListener(v -> {
                Intent intent = new Intent(context, Activity_About.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            });
        }
    }
}
