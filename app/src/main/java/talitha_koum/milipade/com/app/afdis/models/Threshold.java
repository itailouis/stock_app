package talitha_koum.milipade.com.app.afdis.models;

/**
 * Created by TALITHA_KOUM on 6/11/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.network
 */
public class Threshold {
    String product_id,product_size,shop_id,product_price,buffer_level,date_created;

    public Threshold() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getBuffer_level() {
        return buffer_level;
    }

    public void setBuffer_level(String buffer_level) {
        this.buffer_level = buffer_level;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
