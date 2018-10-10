package talitha_koum.milipade.com.app.afdis;

import android.app.Application;
import android.content.Context;

import talitha_koum.milipade.com.app.afdis.utils.MyPreferenceManager;

/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis
 */
public class App extends Application {
    private static App instance;
    private static MyPreferenceManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized App getInstance() {
        return instance;
    }
    public static MyPreferenceManager getPrefManager(Context context) {
        if (pref == null) {
            pref = new MyPreferenceManager(context);
        }

        return pref;
    }

}
