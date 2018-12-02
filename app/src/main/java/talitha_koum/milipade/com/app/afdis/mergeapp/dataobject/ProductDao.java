package talitha_koum.milipade.com.app.afdis.mergeapp.dataobject;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import talitha_koum.milipade.com.app.afdis.mergeapp.models.Product;


/**
 * Created by TALITHA_KOUM on 21/11/2018.
 * file for the  Superviewer. project
 * in talitha_koum.milipade.com.app.superviewer.dataobject
 */
@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Query("DELETE FROM product_table ")
    void deleteAll();

    @Query("SELECT * from product_table GROUP BY product_name ORDER BY product_name ASC")
    LiveData<List<Product>> getAlphabetizedProducts();

    @Query("SELECT * FROM product_table WHERE product_name = :product_name")
    LiveData<List<Product>> findProductName(String product_name);

    @Delete
    void deleteProduct(Product product);


}
