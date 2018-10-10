package talitha_koum.milipade.com.app.afdis.utils;

import android.content.Context;
import android.content.SharedPreferences;

import talitha_koum.milipade.com.app.afdis.models.User;



public class MyPreferenceManager {


    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "afdis_app";

    // All Shared Preferences Keys
    private static final String KEY_IS_LOGIN = "is_user_loggedin";
    private static final String KEY_IP = "ipaddress";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_SHOP_ID = "user_id";
    private static final String KEY_SHOP_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_NOTIFICATIONS = "notifications";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getUser_id());
        editor.putString(KEY_USER_NAME, user.getUsername());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.commit();
        //Log.e(TAG, "User is stored in shared preferences. " + user.getName() + ", " + user.getEmail());
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, email;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);

            User user = new User( email,id,name,name);
            return user;
        }
        return null;
    }
    public String getShopId(){
        return pref.getString(KEY_SHOP_ID, null);
    }
    public String getIP(){
        return pref.getString(KEY_IP, null);
    }
    public String getShopName(){
        return pref.getString(KEY_SHOP_NAME, null);
    }
    public void setShopId(String id){
        editor.putString(KEY_SHOP_ID, id);
        editor.commit();
        
    }
    public void setShopName(String name){
        editor.putString(KEY_SHOP_NAME, name);
        editor.commit();

    }
    public void setIP(String name){
        editor.putString(KEY_IP, name);
        editor.commit();

    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public void setLogin(boolean b) {
        editor.putBoolean(KEY_IS_LOGIN, b);
        editor.commit();
    }

    public boolean isLogedIn() {
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }
}
