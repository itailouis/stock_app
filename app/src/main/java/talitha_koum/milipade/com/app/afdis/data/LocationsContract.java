package talitha_koum.milipade.com.app.afdis.data;

import android.provider.BaseColumns;

public class LocationsContract {
    public static final String DB_NAME = "talitha_koum.milipade.com.app.smsserver.db";
    public static final int DB_VERSION = 1;



    public static abstract class LocationEntry implements BaseColumns {

        public static final String TABLE_LOCATION = "transactions_location";
        public static final String COL_LOCATION_ID = "location_id";
        public static final String COL_LAT = "location_lat";
        public static final String COL_LON = "location_lon";
        public static final String COL_SHOP_ID = "shop_id";
        public static final String COL_TIMESTAMP = "timestamp";

    }
}
