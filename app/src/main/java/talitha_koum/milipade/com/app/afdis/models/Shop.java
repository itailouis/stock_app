package talitha_koum.milipade.com.app.afdis.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.models
 */
public class  Shop implements Parcelable{
    private User user;
    private String shop_id;
    private String shop_name;
    private int shop_location;
    private String  shop_long;
    private String shop_lat;
    private String status;
    private String last_visited;

    public Shop() {
    }

    protected Shop(Parcel in) {
        shop_id = in.readString();
        shop_name = in.readString();
        shop_location = in.readInt();
        shop_long = in.readString();
        shop_lat = in.readString();
        status = in.readString();
        last_visited = in.readString();
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getShop_location() {
        return shop_location;
    }

    public void setShop_location(int shop_location) {
        this.shop_location = shop_location;
    }

    public String getShop_long() {
        return shop_long;
    }

    public void setShop_long(String shop_long) {
        this.shop_long = shop_long;
    }

    public String getShop_lat() {
        return shop_lat;
    }

    public void setShop_lat(String shop_lat) {
        this.shop_lat = shop_lat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLast_visited() {
        return last_visited;
    }

    public void setLast_visited(String last_visited) {
        this.last_visited = last_visited;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shop_id);
        parcel.writeString(shop_name);
        parcel.writeInt(shop_location);
        parcel.writeString(shop_long);
        parcel.writeString(shop_lat);
        parcel.writeString(status);
        parcel.writeString(last_visited);
    }
}
