package talitha_koum.milipade.com.app.afdis.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {

    public AppDbHelper(Context context) {
        super(context, AfidsContract.DB_NAME, null, AfidsContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + AfidsContract.ShopEntry.TABLE_SHOPS + " ( " +
                AfidsContract.ShopEntry.COL_SHOP_ID + " INTEGER PRIMARY KEY , " +
                AfidsContract.ShopEntry.COL_SHOP_NAME+ " TEXT NOT NULL," +
                AfidsContract.ShopEntry.COL_SHOP_LOCATION+ " TEXT NOT NULL," +
                AfidsContract.ShopEntry.COL_SHOP_LAT+ " TEXT NOT NULL," +
                AfidsContract.ShopEntry.COL_SHOP_LON+ " TEXT NOT NULL," +
                AfidsContract.ShopEntry.COL_SHOP_LAST_VISIT+ " TEXT NOT NULL," +
                AfidsContract.ShopEntry.COL_SHOP_STATUS+ " TEXT  );";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AfidsContract.ShopEntry.TABLE_SHOPS);
        onCreate(db);
    }
}
