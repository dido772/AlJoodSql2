package se.aljood.aljoodsql.SQL.Module;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Eyad on 2018-05-20.
 */

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_FIRST_NAME = "firstname";
    private static final String KEY_LAST_NAME = "lasttname";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_PASSWORD = "userpassword";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_REMEMBER_ME ="remember_me";
    private static final String KEY_IS_SELECTED_LANGUAGE = "is_selected_language";
    private static final String KEY_LANGUAGE_VALUE="language_value";
    private static final String KEY_SCREEN_LAYOUT_SIZE="screen_size";
    private SharedPrefManager(Context context){
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public Boolean userLogin(int id,String firstname,String lastname,String username,String email,String pass,Boolean remember_me){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_FIRST_NAME, firstname);
        editor.putString(KEY_LAST_NAME, lastname);
        editor.putString(KEY_USER_NAME,username);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PASSWORD, pass);
        editor.putString(KEY_REMEMBER_ME,String.valueOf(remember_me));
        editor.apply();
        return true;
    }
    public int get_user_ID(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(KEY_USER_ID, 0);
        return id;
    }
    public String get_user_Name(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_name = sharedPreferences.getString(KEY_USER_NAME, null);
        return user_name;
    }

    public String get_First_Name(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String first_name = sharedPreferences.getString(KEY_FIRST_NAME, null);
        return first_name;
    }

    public String get_Last_Name(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String last_name = sharedPreferences.getString(KEY_LAST_NAME, null);
        return last_name;
    }



    public String get_user_Email(){
        String user_email;
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        user_email = sharedPreferences.getString(KEY_USER_EMAIL, null);
        return user_email;
    }
    public String get_user_Pass(){
        String user_pass;
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        user_pass = sharedPreferences.getString(KEY_USER_PASSWORD, null);
        return user_pass;
    }
    public Boolean get_remember_Me(){
        String remember_me;
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        remember_me= sharedPreferences.getString(KEY_REMEMBER_ME, null);
        return Boolean.parseBoolean(remember_me.toString());
    }
    public Boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_NAME,null)!=null){
            return true;
        }
        return false;
    }
    public Boolean isSelectedLanguage(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_IS_SELECTED_LANGUAGE,null)!=null){
            return true;
        }
        return false;
    }
    public String get_Lang(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString(KEY_LANGUAGE_VALUE, null);
        return lang;
    }
    public String get_Screen_Size(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString(KEY_SCREEN_LAYOUT_SIZE, null);
        return lang;
    }
    public void set_Screen_Size(String screen_size_value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SCREEN_LAYOUT_SIZE,screen_size_value);
        editor.apply();
    }
    public void set_Lang(String lang_value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LANGUAGE_VALUE,lang_value);
        editor.putString(KEY_IS_SELECTED_LANGUAGE,lang_value);

        editor.apply();
    }
    public Boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_USER_PASSWORD);
        editor.remove(KEY_REMEMBER_ME);
        editor.apply();
        return true;
    }
}
