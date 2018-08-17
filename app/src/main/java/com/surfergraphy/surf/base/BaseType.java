package com.surfergraphy.surf.base;

import android.text.TextUtils;

import com.surfergraphy.surf.R;

/**
 * Created by ddfactory on 2018-02-21.
 */

public class BaseType {

    public enum LocationType {
        Best_Photo("BP", "Best Photo", R.drawable.ic_menu_camera),
        Event_Promotion("EP", "Event & Promotion", R.drawable.ic_menu_camera),
        Lesson_Photos("LP", "Lesson Photos - First TakeOff", R.drawable.ic_menu_camera),
        Personal_Shoot("PS", "Personal Shoot", R.drawable.ic_menu_camera),
        Korea("KO", "Korea", R.drawable.ko),
        Korea_EastCoast("KOEC", "East Coast", R.drawable.ko),
        Korea_SouthCoast("KOSC", "South Coast", R.drawable.ko),
        Korea_WestCoast("KOWC", "West Coast", R.drawable.ko),
        Korea_JejuIsland("KOJI", "Jeju Island", R.drawable.ko),
        Japan("JP", "Japan", R.drawable.jp),
        China("CN", "China", R.drawable.cn),
        Indonesia("ID", "Indonesia", R.drawable.id),
        Philippines("PH", "Philippines", R.drawable.ph),
        Taiwan("TW", "Taiwan", R.drawable.tw),
        Usa("US", "Usa", R.drawable.us),
        Hawaii("USHW", "Hawaii", R.drawable.us),
        Australia("AU", "Australia", R.drawable.au),
        OtherCountries("ETC", "Other Countries", R.drawable.ic_menu_camera);

        private String code;
        private String name;
        private int drawableId;

        LocationType(String code, String name, int drawableId) {
            this.code = code;
            this.name = name;
            this.drawableId = drawableId;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public int getDrawableId() {
            return drawableId;
        }

        public static LocationType findLocationType(String code) {
            for (LocationType location : LocationType.values()) {
                if (TextUtils.equals(location.getCode(), code))
                    return location;
            }
            return null;
        }
    }

    public enum SignIn {
        NoSessions(null),
        NewSignIn("new_sign_in"),
        Email("g"),
        Kakao("k"),
        Facebook("f"),
        Naver("n");

        private String code;

        SignIn(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public enum OpenType {
        Navigation("navigation"),
        OpenNavigation("open_navigation"),
        Back("back");

        private String code;

        OpenType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
