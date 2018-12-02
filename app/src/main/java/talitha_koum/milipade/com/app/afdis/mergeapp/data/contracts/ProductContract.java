package talitha_koum.milipade.com.app.afdis.mergeapp.data.contracts;

import android.provider.BaseColumns;

/**
 * Created by TALITHA_KOUM on 13/11/2018.
 * file for the  Superviewer. project
 * in talitha_koum.milipade.com.app.superviewer.data.contracts
 */
public class ProductContract {

    public static final String DB_NAME = "talitha_koum.milipade.com.app.superviewer";
    public static final int DB_VERSION = 1;

    public static abstract class ProductsEntry implements BaseColumns {
        public static final String TABLE_PRODUCTS = "products";

        public static final String COL_PRODUCTS_ID = "product_id";
        public static final String COL_PRODUCTS_NAME = "product_name";
        public static final String COL_PRODUCTS_SIZE = "product_size";
        public static final String COL_PRODUCTS_CATIGORY = "product_category";
        public static final String COL_PRODUCTS_IDENTIFIER = "product_identifier";
    }
}
