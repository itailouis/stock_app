package talitha_koum.milipade.com.app.afdis.responses;

import java.util.List;

import talitha_koum.milipade.com.app.afdis.models.Stock;

/**
 * Created by TALITHA_KOUM on 8/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.responses
 */
public class StockResponse {

    private  int status_code ;
    private String message;
    private List<Stock> data;

    public StockResponse() {
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

    public List<Stock> getData() {
        return data;
    }

    public void setData(List<Stock> data) {
        this.data = data;
    }
}
