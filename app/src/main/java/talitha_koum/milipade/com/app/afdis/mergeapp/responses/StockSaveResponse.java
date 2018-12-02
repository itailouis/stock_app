package talitha_koum.milipade.com.app.afdis.mergeapp.responses;

/**
 * Created by TALITHA_KOUM on 9/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.responses
 */
public class StockSaveResponse {
    private  int status_code ;
    private String message;
    private String data;

    public StockSaveResponse() {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
