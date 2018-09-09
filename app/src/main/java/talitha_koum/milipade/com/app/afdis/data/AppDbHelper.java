package talitha_koum.milipade.com.app.afdis.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {

    public AppDbHelper(Context context) {
        super(context, LocationsContract.DB_NAME, null, LocationsContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + LocationsContract.LocationEntry.TABLE_LOCATION + " ( " +
                LocationsContract.LocationEntry.COL_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LocationsContract.LocationEntry.COL_LON+ " TEXT NOT NULL," +
                LocationsContract.LocationEntry.COL_LAT+ " TEXT NOT NULL," +
                LocationsContract.LocationEntry.COL_SHOP_ID+ " TEXT NOT NULL," +
                LocationsContract.LocationEntry.COL_TIMESTAMP+ " TEXT  );";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LocationsContract.LocationEntry.TABLE_LOCATION);
        onCreate(db);
    }
}
