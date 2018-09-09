package talitha_koum.milipade.com.app.afdis.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.models
 */
public class Stock implements Parcelable {

    private String inventory_id;
    private String shop_id;
    private String user_id;
    private String product_id;
    private String product_size;
    private String product_category;
    private String product_quantity;
    private String date_created;
    private String product_name;
    private String product_identifier;

    public Stock() {
    }

    protected Stock(Parcel in) {
        inventory_id = in.readString();
        shop_id = in.readString();
        user_id = in.readString();
        product_id = in.readString();
        product_size = in.readString();
        product_category = in.readString();
        product_quantity = in.readString();
        date_created = in.readString();
        product_name = in.readString();
        product_identifier = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(inventory_id);
        dest.writeString(shop_id);
        dest.writeString(user_id);
        dest.writeString(product_id);
        dest.writeString(product_size);
        dest.writeString(product_category);
        dest.writeString(product_quantity);
        dest.writeString(date_created);
        dest.writeString(product_name);
        dest.writeString(product_identifier);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    public String getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(String inventory_id) {
        this.inventory_id = inventory_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_identifier() {
        return product_identifier;
    }

    public void setProduct_identifier(String product_identifier) {
        this.product_identifier = product_identifier;
    }
}