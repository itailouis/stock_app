package talitha_koum.milipade.com.app.afdis.responses;

import talitha_koum.milipade.com.app.afdis.models.PlanSave;

/**
 * Created by TALITHA_KOUM on 26/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.responses
 */
public class PlanResponse {
    private  int status_code ;
    private String message;
    private PlanSave data;

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

    public PlanSave getData() {
        return data;
    }

    public void setData(PlanSave data) {
        this.data = data;
    }
}
