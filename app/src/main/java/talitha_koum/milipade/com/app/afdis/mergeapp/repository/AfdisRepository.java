package talitha_koum.milipade.com.app.afdis.mergeapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import talitha_koum.milipade.com.app.afdis.mergeapp.database.AfdisDatabase;
import talitha_koum.milipade.com.app.afdis.mergeapp.dataobject.ProductDao;
import talitha_koum.milipade.com.app.afdis.mergeapp.dataobject.StockTackDao;
import talitha_koum.milipade.com.app.afdis.mergeapp.models.Product;
import talitha_koum.milipade.com.app.afdis.mergeapp.models.StockTake;


/**
 * Created by TALITHA_KOUM on 21/11/2018.
 * file for the  Superviewer. project
 * in talitha_koum.milipade.com.app.superviewer.repository
 */
public class AfdisRepository {

    private ProductDao productDao;
    private StockTackDao stockTackDao;
    private LiveData<List<Product>> products;

    public AfdisRepository(Application application) {
        AfdisDatabase db = AfdisDatabase.getDatabase(application);
        productDao = db.productDao();
        stockTackDao= db.stockTackDao();
        products = productDao.getAlphabetizedProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
            return products;
    }

    public void deleteProduct(Product product)  {
        new deleteWordAsyncTask(productDao).execute(product);
    }

    public LiveData<List<Product>> getProduct(String name) {
        return productDao.findProductName(name);
    }

    public void insert(Product product) {
        new insertAsyncTask(productDao).execute(product);
    }

    public void deleteAll() {
        new deleteAllWordsAsyncTask(productDao).execute();
    }

    public void insertStock(StockTake stock) {
        new insertStockAsyncTask(stockTackDao).execute(stock);
    }

    private static class insertAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao mAsyncTaskDao;

        insertAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    private static class insertStockAsyncTask extends AsyncTask<StockTake, Void, Void> {

        private StockTackDao mAsyncTaskDao;

        insertStockAsyncTask(StockTackDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final StockTake... params) {
            mAsyncTaskDao.insertStock(params[0]);
            return null;
        }
    }


    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProductDao mAsyncTaskDao;

        deleteAllWordsAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteWordAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao mAsyncTaskDao;

        deleteWordAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            mAsyncTaskDao.deleteProduct(params[0]);
            return null;
        }
    }
}
