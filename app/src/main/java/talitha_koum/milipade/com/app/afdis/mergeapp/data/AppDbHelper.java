package talitha_koum.milipade.com.app.afdis.mergeapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import talitha_koum.milipade.com.app.superviewer.data.contracts.ProductContract;

public class AppDbHelper extends SQLiteOpenHelper {

    public AppDbHelper(Context context) {
        super(context, ProductContract.DB_NAME, null, ProductContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + ProductContract.ProductsEntry.TABLE_PRODUCTS + " ( " +
                ProductContract.ProductsEntry.COL_PRODUCTS_ID + " INTEGER NOT NULL , " +
                ProductContract.ProductsEntry.COL_PRODUCTS_NAME+ " TEXT NOT NULL," +
                ProductContract.ProductsEntry.COL_PRODUCTS_SIZE+ " TEXT NOT NULL," +
                ProductContract.ProductsEntry.COL_PRODUCTS_CATIGORY+ " TEXT NOT NULL," +
                ProductContract.ProductsEntry.COL_PRODUCTS_IDENTIFIER+ " TEXT NOT NULL" +")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProductContract.ProductsEntry.TABLE_PRODUCTS);
        onCreate(db);
    }
}
