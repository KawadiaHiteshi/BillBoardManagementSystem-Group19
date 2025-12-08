package Business.Billboard;

import Business.UserAccount.UserAccount;
import java.util.Date;

/**
 * Represents a user waiting for a billboard to become available.
 */
public class WaitlistEntry {
    private UserAccount user;
    private Date requestDate;
    private String message;

    public WaitlistEntry(UserAccount user, String message) {
        this.user = user;
        this.message = message;
        this.requestDate = new Date();
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return user.getUsername() + " (since " + requestDate + ")";
    }
}
