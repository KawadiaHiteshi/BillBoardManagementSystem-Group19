package Business.Billboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Business.UserAccount.UserAccount;

/**
 * Represents a billboard in the ecosystem
 */
public class Billboard {

    private int boardId;
    private String location;
    private String size; // e.g., "14x48", "10x30"
    private String type; // e.g., "Digital", "Static", "LED"
    private double pricePerDay;
    private BillboardStatus status;
    private String description;
    private boolean isLighted;
    private List<BookingRecord> bookingHistory;

    private static int counter = 1000;

    public Billboard(String location, String size, String type, double pricePerDay, String description,
            boolean isLighted) {
        this.boardId = counter++;
        this.location = location;
        this.size = size;
        this.type = type;
        this.pricePerDay = pricePerDay;
        this.description = description;
        this.isLighted = isLighted;
        this.status = BillboardStatus.AVAILABLE;
        this.bookingHistory = new ArrayList<>();
    }

    // Getters and Setters
    public int getBoardId() {
        return boardId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public BillboardStatus getStatus() {
        return status;
    }

    public void setStatus(BillboardStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLighted() {
        return isLighted;
    }

    public void setLighted(boolean lighted) {
        isLighted = lighted;
    }

    public List<BookingRecord> getBookingHistory() {
        return bookingHistory;
    }

    public void addBooking(Date startDate, Date endDate, String clientName) {
        BookingRecord record = new BookingRecord(startDate, endDate, clientName);
        bookingHistory.add(record);
    }

    public boolean isAvailableForDates(Date startDate, Date endDate) {
        if (status != BillboardStatus.AVAILABLE && status != BillboardStatus.BOOKED) {
            return false;
        }

        for (BookingRecord record : bookingHistory) {
            if (record.overlaps(startDate, endDate)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Billboard #" + boardId + " - " + location + " (" + size + ")";
    }

    /**
     * Inner class to track booking history
     */
    public static class BookingRecord {
        private Date startDate;
        private Date endDate;
        private String clientName;
        private Date bookingDate;

        public BookingRecord(Date startDate, Date endDate, String clientName) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.clientName = clientName;
            this.bookingDate = new Date();
        }

        public boolean overlaps(Date checkStart, Date checkEnd) {
            return !(checkEnd.before(startDate) || checkStart.after(endDate));
        }

        public Date getStartDate() {
            return startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public String getClientName() {
            return clientName;
        }

        public Date getBookingDate() {
            return bookingDate;
        }
    }

    // Maintenance History
    private List<MaintenanceRecord> maintenanceHistory = new ArrayList<>();

    public List<MaintenanceRecord> getMaintenanceHistory() {
        return maintenanceHistory;
    }

    public void addMaintenanceRecord(MaintenanceRecord record) {
        maintenanceHistory.add(record);
    }

    // Waitlist
    private List<WaitlistEntry> waitlist = new ArrayList<>();

    public List<WaitlistEntry> getWaitlist() {
        return waitlist;
    }

    public void joinWaitlist(UserAccount user, String message) {
        waitlist.add(new WaitlistEntry(user, message));
    }
}
