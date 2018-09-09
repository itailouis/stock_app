package talitha_koum.milipade.com.app.afdis.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.models
 */
public class Orders implements Parcelable{

    private String order_id;
    private String product_id;
    private String quantity_order;
    private String delivery_date;
    private String delivered_quantity;
    private String proposed_delivery_date;
    private String order_status;
    private String date_created;
    private String shop_id;
    private String product_name;
    private String  product_identifier;
    private String  product_category;
    private String product_size;

    public Orders() {
    }

    protected Orders(Parcel in) {
        order_id = in.readString();
        product_id = in.readString();
        quantity_order = in.readString();
        delivery_date = in.readString();
        delivered_quantity = in.readString();
        proposed_delivery_date = in.readString();
        order_status = in.readString();
        date_created = in.readString();
        shop_id = in.readString();
        product_name = in.readString();
        product_identifier = in.readString();
        product_category = in.readString();
        product_size = in.readString();
    }

    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        @Override
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        @Override
        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity_order() {
        return quantity_order;
    }

    public void setQuantity_order(String quantity_order) {
        this.quantity_order = quantity_order;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivered_quantity() {
        return delivered_quantity;
    }

    public void setDelivered_quantity(String delivered_quantity) {
        this.delivered_quantity = delivered_quantity;
    }

    public String getProposed_delivery_date() {
        return proposed_delivery_date;
    }

    public void setProposed_delivery_date(String proposed_delivery_date) {
        this.proposed_delivery_date = proposed_delivery_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
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

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(order_id);
        parcel.writeString(product_id);
        parcel.writeString(quantity_order);
        parcel.writeString(delivery_date);
        parcel.writeString(delivered_quantity);
        parcel.writeString(proposed_delivery_date);
        parcel.writeString(order_status);
        parcel.writeString(date_created);
        parcel.writeString(shop_id);
        parcel.writeString(product_name);
        parcel.writeString(product_identifier);
        parcel.writeString(product_category);
        parcel.writeString(product_size);
    }
}
