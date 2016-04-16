package pers.nbu.netcoursetea.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Chenss on 2015/8/18.
 */
public class PreferenceUtils {
    public static final String PREFERENCE = "PREFERENCE";


    public static final String PREFERENCE_USERNAME = "PREFERENCE_USERNAME";
    public static final String PREFERENCE_USERID = "PREFERENCE_USERID";
    //保存密码
    public static final String PREFERENCE_PASSWORD = "PREFERENCE_PASSWORD";

    public static final String PREFERENCE_POST = "PREFERENCE_POST";
    public static final String PREFERENCE_MOD = "PREFERENCE_MOD";

    //Account login
    public static Boolean LOGINVAL =false;

    public static void saveLoginInfo(Context context,String userId,String pwd){
        SharedPreferences sp=context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(PREFERENCE_USERID, userId);
        editor.putString(PREFERENCE_PASSWORD, pwd);
        editor.commit();
    }

    public static void saveClassInfo(Context context,String userName,String post,String mod){
        SharedPreferences sp=context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(PREFERENCE_USERNAME,userName);
        editor.putString(PREFERENCE_POST,post);
        editor.putString(PREFERENCE_MOD,mod);
        editor.commit();
    }

    public static String getUserName(Context context){
        SharedPreferences sp=context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString(PREFERENCE_USERNAME,"");
    }

    public static String getPost(Context context){
        SharedPreferences sp=context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString(PREFERENCE_POST,"");
    }

    public static String getMod(Context context){
        SharedPreferences sp=context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString(PREFERENCE_MOD,"");
    }

    public static String getUserId(Context context){
        SharedPreferences sp=context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString(PREFERENCE_USERID,"");
    }

    public static Boolean getLOGINVAL() {
        return LOGINVAL;
    }

    public static void setLOGINVAL(Boolean LOGINVAL) {
        PreferenceUtils.LOGINVAL = LOGINVAL;
    }
}
