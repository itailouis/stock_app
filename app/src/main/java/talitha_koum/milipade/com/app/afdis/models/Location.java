package talitha_koum.milipade.com.app.afdis.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by TALITHA_KOUM on 20/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.models
 */
public class Location implements Parcelable {
    private String location_id;
    private String location_name;
    private String location_radius;
    private String centre_lat;
    private String centre_long;
    private String user_id;
    private ArrayList<Shop> shops;

    protected Location(Parcel in) {
        location_id = in.readString();
        location_name = in.readString();
        location_radius = in.readString();
        centre_lat = in.readString();
        centre_long = in.readString();
        user_id = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLocation_radius() {
        return location_radius;
    }

    public void setLocation_radius(String location_radius) {
        this.location_radius = location_radius;
    }

    public String getCentre_lat() {
        return centre_lat;
    }

    public void setCentre_lat(String centre_lat) {
        this.centre_lat = centre_lat;
    }

    public String getCentre_long() {
        return centre_long;
    }

    public void setCentre_long(String centre_long) {
        this.centre_long = centre_long;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<Shop> getShops() {
        return shops;
    }

    public void setShops(ArrayList<Shop> shops) {
        this.shops = shops;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(location_id);
        parcel.writeString(location_name);
        parcel.writeString(location_radius);
        parcel.writeString(centre_lat);
        parcel.writeString(centre_long);
        parcel.writeString(user_id);
    }
}
