package Business.WorkQueue;

import Business.UserAccount.UserAccount;
import java.util.Date;

public abstract class WorkRequest {

    private UserAccount sender;
    private UserAccount receiver;
    private String status;
    private String message;
    private Date requestDate;
    private Date resolveDate;
    private int requestId;
    private static int count = 1;

    public WorkRequest() {
        requestDate = new Date();
        status = "Pending";
        requestId = count;
        count++;
    }

    // Getters & setters
    public int getRequestId() {
        return requestId;
    }

    public UserAccount getSender() {
        return sender;
    }

    public void setSender(UserAccount sender) {
        this.sender = sender;
    }

    public UserAccount getReceiver() {
        return receiver;
    }

    public void setReceiver(UserAccount receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public Date getResolveDate() {
        return resolveDate;
    }

    public void setResolveDate(Date resolveDate) {
        this.resolveDate = resolveDate;
    }

    @Override
    public String toString() {
        return String.valueOf(requestId);
    }
}
