package Business.Billboard;

/**
 * Enum representing the various states a billboard can be in
 */
public enum BillboardStatus {
    AVAILABLE("Available for Booking"),
    BOOKED("Currently Booked"),
    MAINTENANCE("Under Maintenance"),
    PENDING_PERMIT("Pending Permit Approval"),
    REJECTED("Permit Rejected");

    private String displayName;

    BillboardStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
