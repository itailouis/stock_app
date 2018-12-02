package talitha_koum.milipade.com.app.afdis.mergeapp.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by TALITHA_KOUM on 13/11/2018.
 * file for the  Superviewer. project
 * in talitha_koum.milipade.com.app.superviewer.models
 */

@Entity(tableName = "product_table")
public class Product {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "local_product_id")
    private int local_product_id;

    @NonNull
    private String product_id;
    @NonNull
    private String product_name;
    @NonNull
    private String product_identifier;
    @NonNull
    private String product_category;
    @NonNull
    private String product_size;



    public Product() {
    }

    @NonNull
    public int getLocal_product_id() {
        return local_product_id;
    }

    public void setLocal_product_id(@NonNull int local_product_id) {
        this.local_product_id = local_product_id;
    }

    @NonNull
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(@NonNull String product_id) {
        this.product_id = product_id;
    }

    @NonNull
    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(@NonNull String product_name) {
        this.product_name = product_name;
    }

    @NonNull
    public String getProduct_identifier() {
        return product_identifier;
    }

    public void setProduct_identifier(@NonNull String product_identifier) {
        this.product_identifier = product_identifier;
    }

    @NonNull
    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(@NonNull String product_category) {
        this.product_category = product_category;
    }

    @NonNull
    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(@NonNull String product_size) {
        this.product_size = product_size;
    }
}
