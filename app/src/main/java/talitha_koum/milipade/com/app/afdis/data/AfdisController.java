package talitha_koum.milipade.com.app.afdis.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import talitha_koum.milipade.com.app.afdis.models.Shop;

public class AfdisController {
	private AppDbHelper dbhelper;
	private Context ourcontext;
	private SQLiteDatabase database;
	public AfdisController(Context c) {
		ourcontext = c;
	}
	public AfdisController open() throws SQLException {
		dbhelper = new AppDbHelper(ourcontext);
		database = dbhelper.getWritableDatabase();
		return this;
	}
	public void close() {
		dbhelper.close();
	}
	public void insertData(Shop shop,String locationName) {
		ContentValues cv = new ContentValues();
		cv.put(AfidsContract.ShopEntry.COL_SHOP_ID, shop.getShop_id());
		cv.put(AfidsContract.ShopEntry.COL_SHOP_NAME, shop.getShop_name());
		cv.put(AfidsContract.ShopEntry.COL_SHOP_LOCATION,locationName);
		cv.put(AfidsContract.ShopEntry.COL_SHOP_LAT,shop.getShop_lat());
		cv.put(AfidsContract.ShopEntry.COL_SHOP_LON, shop.getShop_long());
		cv.put(AfidsContract.ShopEntry.COL_SHOP_STATUS,shop.getStatus());
		cv.put(AfidsContract.ShopEntry.COL_SHOP_LAST_VISIT,shop.getLast_visited());

		database.insert(AfidsContract.ShopEntry.TABLE_SHOPS,null, cv);
	}
	public Cursor readEntry() {
		String[] allColumns = new String[]{
				AfidsContract.ShopEntry.COL_SHOP_ID,
				AfidsContract.ShopEntry.COL_SHOP_NAME,
				AfidsContract.ShopEntry.COL_SHOP_LOCATION,
				AfidsContract.ShopEntry.COL_SHOP_LAT,
				AfidsContract.ShopEntry.COL_SHOP_LON,
				AfidsContract.ShopEntry.COL_SHOP_STATUS,
				AfidsContract.ShopEntry.COL_SHOP_LAST_VISIT};
		Cursor c = database.query(AfidsContract.ShopEntry.TABLE_SHOPS, allColumns, null, null, null,null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}}
