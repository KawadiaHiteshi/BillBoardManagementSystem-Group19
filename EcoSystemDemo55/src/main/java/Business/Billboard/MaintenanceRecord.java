package Business.Billboard;

import java.util.Date;

/**
 * Represents a maintenance record for a billboard
 */
public class MaintenanceRecord {

    private Date date;
    private String description;
    private String type; // e.g., "Maintenance", "Power Issue"
    private String status; // "Open", "Closed"
    private String resolution;

    public MaintenanceRecord(String description, String type) {
        this.date = new Date();
        this.description = description;
        this.type = type;
        this.status = "Open";
        this.resolution = "";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public String toString() {
        return type + " - " + status + " (" + date + ")";
    }
}
