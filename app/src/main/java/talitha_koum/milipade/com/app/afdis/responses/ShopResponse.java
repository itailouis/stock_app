package talitha_koum.milipade.com.app.afdis.responses;

import java.util.List;

import talitha_koum.milipade.com.app.afdis.models.Shop;

/**
 * Created by TALITHA_KOUM on 8/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.responses
 */
public class ShopResponse  {

    private  int status_code ;
    private String message;
    private List<Shop> data;

    public ShopResponse() {
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Shop> getData() {
        return data;
    }

    public void setData(List<Shop> data) {
        this.data = data;
    }
}
