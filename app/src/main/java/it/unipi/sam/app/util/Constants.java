package it.unipi.sam.app.util;

public class Constants {
    public static final int TIMESTAMP = 0;
    public static final int PATH = 1;

    public static String restBasePath = "https://donow.cloud/volleycecina/";
    public static String firstRestReqPath = "restInfo.json";

    public static String teamCode = "team_code";
    public static String peopleCode = "people_code";

    public static String teamInstance = "ti";
    public static String personInstance = "pi";

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

    // intent bundle keys
    public static String rest_info_instance_key = "rest_info_instance_key";
    public static String news_id_key = "newsid";
    public static String purpose_key = "PURPOSE";

    public static String resource_path = "path";
    public static String resource_name = "name";
    public static String resource_tag = "tag";

    // Room
    public static String database_name = "VolleyCecinaFavorites";
}
