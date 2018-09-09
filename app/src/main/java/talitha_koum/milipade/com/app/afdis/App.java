package talitha_koum.milipade.com.app.afdis;

import android.app.Application;

import talitha_koum.milipade.com.app.afdis.utils.MyPreferenceManager;

/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis
 */
public class App extends Application {
    private static App instance;
    private MyPreferenceManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized App getInstance() {
        return instance;
    }
    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }

}
