package talitha_koum.milipade.com.app.afdis.responses;

import talitha_koum.milipade.com.app.afdis.models.User;

/**
 * Created by TALITHA_KOUM on 24/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.responses
 */
public class Login {
    private boolean status;
    private String message;
    private User user;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
