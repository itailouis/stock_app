package talitha_koum.milipade.com.app.afdis.mergeapp.responses;

import java.util.List;

import talitha_koum.milipade.com.app.afdis.mergeapp.models.ServerProduct;


/**
 * Created by TALITHA_KOUM on 11/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.responses
 */
public class ProductSizeResponse {

    private  int status_code ;
    private String message;
    private List<ServerProduct> data;

    public ProductSizeResponse() {
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

    public List<ServerProduct> getData() {
        return data;
    }

    public void setData(List<ServerProduct> data) {
        this.data = data;
    }
}
