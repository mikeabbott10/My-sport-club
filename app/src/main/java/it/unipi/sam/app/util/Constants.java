package it.unipi.sam.app.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class Constants {
    public static final int TIMESTAMP = 0;
    public static final int PATH = 1;

    public static final int NEWS_FRAGMENT = 10;
    public static final int FAVORITES_FRAGMENT = 11;
    public static final int FEMALE_FRAGMENT = 12;
    public static final int MALE_FRAGMENT = 13;
    public static final int CONTACTS_FRAGMENT = 14;
    public static final int SETTINGS_FRAGMENT = 15;
    public static final int ADVANCED_FRAGMENT = 16;

    public static String restBasePath = "https://donow.cloud/volleycecina/";
    public static String firstRestReqPath = "restInfo.json";

    public static String teamCode = "team_code";
    public static String peopleCode = "people_code";

    public static String coverImage = "cover_image";
    public static String presentationImage = "presentation_image";
    public static String profileImage = "profile_image";
    public static String infoFile = "info_file";

    // sharedPreferences stuff
    public static String news_key = "news";
    public static String teams_key = "teams";
    public static String people_key = "people";
    public static String timestamp_key = "timestamp";
    public static String id_key = "_id";
    public static String dontaskfordomainverification_key = "dafdv"; // deve essere uguale a @string/dontaskfordomainverification_key
    public static String dontshowcontactspopup_key = "dscp";
    public static String nightmode_key = "nm";
    public static String notification_enabled_key = "nek";

    //savedInstanceState
    public static String already_asked_for_domain_approvation_this_time = "aafdatt";
    public static String last_fragment_key = "lfk";

    // intent bundle keys
    public static String manually_saved_instance_state = "msis";
    public static String rest_info_instance_key = "rest_info_instance_key";
    public static String news_id_key = "newsid";

    public static String resource_path = "path";
    public static String resource_name = "name";
    public static String resource_tag = "tag";

    // Room
    public static String database_name = "VolleyCecinaFavorites";

    //public static final String lat_lon_marker_key = "llmk";


    /**
     * Drawable to Bitmap
     * @param drawable
     * @param widthPixels
     * @param heightPixels
     * @return
     */
    public static Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }
}
