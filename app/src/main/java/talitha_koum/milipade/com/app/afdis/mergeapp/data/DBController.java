package talitha_koum.milipade.com.app.afdis.mergeapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import talitha_koum.milipade.com.app.superviewer.data.contracts.ProductContract;
import talitha_koum.milipade.com.app.superviewer.models.Product;

public class DBController {
	private AppDbHelper dbhelper;
	private Context ourcontext;
	private SQLiteDatabase database;
	public DBController(Context c) {
		ourcontext = c;
	}
	public DBController open() throws SQLException {
		dbhelper = new AppDbHelper(ourcontext);
		database = dbhelper.getWritableDatabase();
		return this;
	}
	public void close() {
		dbhelper.close();
	}
	public void insertData(Product product) {
		ContentValues cv = new ContentValues();
		cv.put(ProductContract.ProductsEntry.COL_PRODUCTS_ID, product.getProduct_id());
		cv.put(ProductContract.ProductsEntry.COL_PRODUCTS_NAME, product.getProduct_name());
		cv.put(ProductContract.ProductsEntry.COL_PRODUCTS_CATIGORY,product.getProduct_category());
		cv.put(ProductContract.ProductsEntry.COL_PRODUCTS_IDENTIFIER,product.getProduct_identifier());
		cv.put(ProductContract.ProductsEntry.COL_PRODUCTS_SIZE, product.getProduct_size());


		database.insert(ProductContract.ProductsEntry.TABLE_PRODUCTS,null, cv);
	}
	public Cursor readEntry() {
		String[] allColumns = new String[]{
				ProductContract.ProductsEntry.COL_PRODUCTS_ID,
				ProductContract.ProductsEntry.COL_PRODUCTS_NAME,
				ProductContract.ProductsEntry.COL_PRODUCTS_CATIGORY,
				ProductContract.ProductsEntry.COL_PRODUCTS_IDENTIFIER,
				ProductContract.ProductsEntry.COL_PRODUCTS_SIZE
		};
		Cursor c = database.query(ProductContract.ProductsEntry.TABLE_PRODUCTS, allColumns, null, null, null,null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
}
