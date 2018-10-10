package talitha_koum.milipade.com.app.afdis.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by TALITHA_KOUM on 12/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.models
 */
public class InventoryHistory implements Parcelable {
    private String date_created;
    private ArrayList<Stock> stocks;

    protected InventoryHistory(Parcel in) {
        date_created = in.readString();
        stocks = in.createTypedArrayList(Stock.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date_created);
        dest.writeTypedList(stocks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InventoryHistory> CREATOR = new Creator<InventoryHistory>() {
        @Override
        public InventoryHistory createFromParcel(Parcel in) {
            return new InventoryHistory(in);
        }

        @Override
        public InventoryHistory[] newArray(int size) {
            return new InventoryHistory[size];
        }
    };

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(ArrayList<Stock> stocks) {
        this.stocks = stocks;
    }
}
