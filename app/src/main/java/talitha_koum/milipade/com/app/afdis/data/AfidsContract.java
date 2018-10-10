package talitha_koum.milipade.com.app.afdis.data;

import android.provider.BaseColumns;

/**
 * Created by TALITHA_KOUM on 24/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.data
 */
public class AfidsContract {
    public static final String DB_NAME = "talitha_koum.milipade.com.app.afdis.data";
    public static final int DB_VERSION = 1;

    public static abstract class ShopEntry implements BaseColumns {
        public static final String TABLE_SHOPS = "shops";
        public static final String COL_SHOP_ID = "shop_id";
        public static final String COL_SHOP_NAME = "shop_name";
        public static final String COL_SHOP_LOCATION = "shop_location";
        public static final String COL_SHOP_LAT = "shop_long";
        public static final String COL_SHOP_LON = "shop_lat";
        public static final String COL_SHOP_STATUS = "status";
        public static final String COL_SHOP_LAST_VISIT = "last_visited";

    }

}
