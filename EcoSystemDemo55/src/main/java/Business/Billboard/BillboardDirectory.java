package Business.Billboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Directory to manage all billboards in the system
 */
public class BillboardDirectory {

    private ArrayList<Billboard> billboards;

    public BillboardDirectory() {
        this.billboards = new ArrayList<>();
    }

    public Billboard createBillboard(String location, String size, String type,
            double pricePerDay, String description, boolean isLighted) {
        Billboard billboard = new Billboard(location, size, type, pricePerDay, description, isLighted);
        billboards.add(billboard);
        return billboard;
    }

    public ArrayList<Billboard> getBillboards() {
        return billboards;
    }

    public Billboard getBillboardById(int boardId) {
        for (Billboard billboard : billboards) {
            if (billboard.getBoardId() == boardId) {
                return billboard;
            }
        }
        return null;
    }

    public List<Billboard> getBillboardsByLocation(String location) {
        return billboards.stream()
                .filter(b -> b.getLocation().toLowerCase().contains(location.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Billboard> getBillboardsByStatus(BillboardStatus status) {
        return billboards.stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Billboard> getAvailableBillboards() {
        return getBillboardsByStatus(BillboardStatus.AVAILABLE);
    }

    public List<Billboard> getBillboardsByType(String type) {
        return billboards.stream()
                .filter(b -> b.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<Billboard> searchBillboards(String location, String type,
            Date startDate, Date endDate) {
        return billboards.stream()
                .filter(b -> location == null || location.isEmpty() ||
                        b.getLocation().toLowerCase().contains(location.toLowerCase()))
                .filter(b -> type == null || type.isEmpty() ||
                        b.getType().equalsIgnoreCase(type))
                .filter(b -> startDate == null || endDate == null ||
                        b.isAvailableForDates(startDate, endDate))
                .collect(Collectors.toList());
    }

    public boolean deleteBillboard(int boardId) {
        Billboard billboard = getBillboardById(boardId);
        if (billboard != null) {
            billboards.remove(billboard);
            return true;
        }
        return false;
    }

    public int getTotalBillboards() {
        return billboards.size();
    }

    public int getAvailableBillboardsCount() {
        return (int) billboards.stream()
                .filter(b -> b.getStatus() == BillboardStatus.AVAILABLE)
                .count();
    }

    public int getBookedBillboardsCount() {
        return (int) billboards.stream()
                .filter(b -> b.getStatus() == BillboardStatus.BOOKED)
                .count();
    }

    public double getTotalRevenuePotential() {
        return billboards.stream()
                .filter(b -> b.getStatus() == BillboardStatus.BOOKED)
                .mapToDouble(Billboard::getPricePerDay)
                .sum();
    }
}
