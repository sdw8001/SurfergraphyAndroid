package com.surfergraphy.surf.base;

import android.text.TextUtils;

/**
 * Created by ddfactory on 2018-02-21.
 */

public class BaseType {

    public enum LocationType {
        Personal_Shoot("PS", "Personal Shoot"),
        Korea_EastCoast("KOEC", "Korea - East Coast"),
        Korea_SouthCoast("KOSC", "Korea - South Coast"),
        Korea_WestCoast("KOWC", "Korea - West Coast"),
        Korea_JejuIsland("KOJI", "Korea - Jeju Island"),
        Japan("JP", "Japan"),
        China("CN", "China"),
        Indonesia("ID", "Indonesia"),
        Philippines("PH", "Philippines"),
        Taiwan("TW", "Taiwan"),
        Usa("US", "Usa"),
        Hawaii("USHW", "Hawaii"),
        Australia("AU", "Australia"),
        OtherCountries("ETC", "Other Countries");

        private String code;
        private String name;

        LocationType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
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
