package talitha_koum.milipade.com.app.afdis.mergeapp.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by TALITHA_KOUM on 18/11/2018.
 * file for the  Superviewer. project
 * in talitha_koum.milipade.com.app.superviewer.models
 */
@Entity(tableName = "inventory_table")
public class StockTake {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "inventory_id")
    int inventory_id;
    String product_id_,
    created_date,
    ShopId,
    UserId,
    product_quantity,
    product_breakages,
    product_size,
    product_inline,
    product_total_shelf,
    Price,
    competitor_name_a,
    competitor_name_b,
    competitor_price_a,
    competitor_price_b,
           expiry,
           near_date,
           imagefile;

    public StockTake() {
    }

    @NonNull
    public int  getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(@NonNull int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public String getProduct_id_() {
        return product_id_;
    }

    public void setProduct_id_(String product_id_) {
        this.product_id_ = product_id_;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        ShopId = shopId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_breakages() {
        return product_breakages;
    }

    public void setProduct_breakages(String product_breakages) {
        this.product_breakages = product_breakages;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public String getProduct_inline() {
        return product_inline;
    }

    public void setProduct_inline(String product_inline) {
        this.product_inline = product_inline;
    }

    public String getProduct_total_shelf() {
        return product_total_shelf;
    }

    public void setProduct_total_shelf(String product_total_shelf) {
        this.product_total_shelf = product_total_shelf;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getCompetitor_name_a() {
        return competitor_name_a;
    }

    public void setCompetitor_name_a(String competitor_name_a) {
        this.competitor_name_a = competitor_name_a;
    }

    public String getCompetitor_name_b() {
        return competitor_name_b;
    }

    public void setCompetitor_name_b(String competitor_name_b) {
        this.competitor_name_b = competitor_name_b;
    }

    public String getCompetitor_price_a() {
        return competitor_price_a;
    }

    public void setCompetitor_price_a(String competitor_price_a) {
        this.competitor_price_a = competitor_price_a;
    }

    public String getCompetitor_price_b() {
        return competitor_price_b;
    }

    public void setCompetitor_price_b(String competitor_price_b) {
        this.competitor_price_b = competitor_price_b;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getNear_date() {
        return near_date;
    }

    public void setNear_date(String near_date) {
        this.near_date = near_date;
    }

    public String getImagefile() {
        return imagefile;
    }

    public void setImagefile(String imagefile) {
        this.imagefile = imagefile;
    }
}
