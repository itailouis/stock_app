package talitha_koum.milipade.com.app.afdis.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by TALITHA_KOUM on 12/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.models
 */
public class OrdersHistory implements Parcelable {

    private String date_created;
    private ArrayList<Orders> orders;

    protected OrdersHistory(Parcel in) {
        date_created = in.readString();
        orders = in.createTypedArrayList(Orders.CREATOR);
    }

    public static final Creator<OrdersHistory> CREATOR = new Creator<OrdersHistory>() {
        @Override
        public OrdersHistory createFromParcel(Parcel in) {
            return new OrdersHistory(in);
        }

        @Override
        public OrdersHistory[] newArray(int size) {
            return new OrdersHistory[size];
        }
    };

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public ArrayList<Orders> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Orders> orders) {
        this.orders = orders;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date_created);
        parcel.writeTypedList(orders);
    }
}
