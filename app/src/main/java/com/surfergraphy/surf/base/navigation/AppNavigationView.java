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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.surfergraphy.surf.R;
import com.surfergraphy.surf.about.Activity_About;
import com.surfergraphy.surf.adapter.NavExpandableListAdapter;
import com.surfergraphy.surf.album.Activity_Album;
import com.surfergraphy.surf.base.ActivityCode;
import com.surfergraphy.surf.base.BaseIntentKey;
import com.surfergraphy.surf.base.BaseType;
import com.surfergraphy.surf.base.activities.BaseActivity;
import com.surfergraphy.surf.base.data.Nation;
import com.surfergraphy.surf.like.Activity_LikePhoto;
import com.surfergraphy.surf.login.ViewModel_Login;
import com.surfergraphy.surf.photos.Activity_Photos;
import com.surfergraphy.surf.photos.ViewModel_Photo;
import com.surfergraphy.surf.utils.CommonTask;
import com.surfergraphy.surf.wavepurchase.Activity_WavePurchase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppNavigationView extends NavigationView {
    private Context context;
    private int currentActivityCode;
    private ViewModel_Login viewModelLogin;
    private ViewModel_Photo viewModelPhoto;
    private String loginType;
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
        headerViewHolder.logout.setOnClickListener(v -> {
            viewModelLogin.logoutAccount(loginType);
            DrawerLayout drawer = (DrawerLayout) ((BaseActivity) context).findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        });
        headerViewHolder.expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                selectMenuLocation(headerViewHolder.adapter.getChild(groupPosition, childPosition));
                return true;
            }
        });
        headerViewHolder.expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (headerViewHolder.adapter.getChildrenCount(groupPosition) == 0) {
                    selectMenuLocation(headerViewHolder.adapter.getGroup(groupPosition).getNationLocationType());
                }
                return false;
            }
        });
        setItemIconTintList(null);
        viewModelLogin.getLoginMemberLiveData().observe((BaseActivity) context, loginMember -> {
            if (loginMember != null) {
                loginType = loginMember.JoinType;
                headerViewHolder.nickName.setText(loginMember.Name);
                headerViewHolder.email.setText(loginMember.Email);
                headerViewHolder.wave.setText(String.valueOf(loginMember.Wave));
                Glide.with(context).load(loginMember.ImageUrl).thumbnail(0.1f).into(headerViewHolder.memberImage);
            }
        });
    }

    public void selectMenuLocation(BaseType.LocationType locationType) {
        if (currentActivityCode != ActivityCode.ACTIVITY_PHOTOS) {
            Intent intent = new Intent(context, Activity_Photos.class);
            intent.putExtra("place", locationType);
            context.startActivity(intent);
            ((Activity) context).finish();
            return;
        }
        viewModelPhoto.deleteDatePhotos();
        viewModelPhoto.deletePhotos();
        viewModelPhoto.setPlace(locationType);
        viewModelPhoto.dataSyncDatesFromPlace(locationType);
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

        @BindView(R.id.expandable_list_view)
        ExpandableListView expandableListView;
        NavExpandableListAdapter adapter;

        @BindView(R.id.logout)
        FrameLayout logout;

        HeaderViewHolder(Context context, View view) {
            ButterKnife.bind(this, view);
            ArrayList<Nation> surfingSpots = new ArrayList<>();
            surfingSpots.add(new Nation(BaseType.LocationType.Best_Photo, new ArrayList<>()));
            surfingSpots.add(new Nation(BaseType.LocationType.Event_Promotion, new ArrayList<>()));
            surfingSpots.add(new Nation(BaseType.LocationType.Lesson_Photos, new ArrayList<>()));
            surfingSpots.add(new Nation(BaseType.LocationType.Personal_Shoot, new ArrayList<>()));
            ArrayList<BaseType.LocationType> childList = new ArrayList<>();
            childList.add(BaseType.LocationType.Korea_EastCoast);
            childList.add(BaseType.LocationType.Korea_WestCoast);
            childList.add(BaseType.LocationType.Korea_SouthCoast);
            childList.add(BaseType.LocationType.Korea_JejuIsland);
            surfingSpots.add(new Nation(BaseType.LocationType.Korea, childList));
            surfingSpots.add(new Nation(BaseType.LocationType.Japan, new ArrayList<>()));
            surfingSpots.add(new Nation(BaseType.LocationType.China, new ArrayList<>()));
            surfingSpots.add(new Nation(BaseType.LocationType.Indonesia, new ArrayList<>()));
            surfingSpots.add(new Nation(BaseType.LocationType.Philippines, new ArrayList<>()));
            surfingSpots.add(new Nation(BaseType.LocationType.Taiwan, new ArrayList<>()));
            surfingSpots.add(new Nation(BaseType.LocationType.Usa, new ArrayList<>()));
            surfingSpots.add(new Nation(BaseType.LocationType.Hawaii, new ArrayList<>()));
            surfingSpots.add(new Nation(BaseType.LocationType.Australia, new ArrayList<>()));
            surfingSpots.add(new Nation(BaseType.LocationType.OtherCountries, new ArrayList<>()));
            adapter = new NavExpandableListAdapter(context, surfingSpots);
            expandableListView.setAdapter(adapter);
            expandableListView.getLayoutParams().height = CommonTask.getDPValue(context, 62) * adapter.getGroupCount() + CommonTask.getDPValue(context, 10);

            int childGenericHeight = CommonTask.getDPValue(context, 52);
            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    expandableListView.getLayoutParams().height += childGenericHeight * adapter.getChildrenCount(groupPosition);
                }
            });
            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {
                    expandableListView.getLayoutParams().height -= childGenericHeight * adapter.getChildrenCount(groupPosition);
                }
            });

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
                intent.putExtra(BaseIntentKey.OpenType, BaseType.OpenType.Navigation);
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
