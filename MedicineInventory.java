//! temporary cuz i need this to test my module -- Noah
import java.util.ArrayList;

public class MedicineInventory {
    private String inventoryId;
    private ArrayList<Medicine> medicineList;

    public MedicineInventory(String inventoryId) {
        this.inventoryId = inventoryId;
        this.medicineList = new ArrayList<>();
    }

    public void addMedicine(Medicine medicine) {
        medicineList.add(medicine);
        System.out.println("Medicine added: " + medicine.getMedicineName());
    }

    public void removeMedicine(String medicineId) {
        medicineList.removeIf(m -> m.getMedicineId().equals(medicineId));
    }

    public void updateStock(String medicineId, int quantity) {
        for (Medicine m : medicineList) {
            if (m.getMedicineId().equals(medicineId)) {
                m.setStockQuantity(quantity);
                return;
            }
        }
    }

    public int checkStock(String medicineId) {
        for (Medicine m : medicineList) {
            if (m.getMedicineId().equals(medicineId)) {
                return m.getStockQuantity();
            }
        }
        return -1;
    }

    public void dispenseMedicine(String medicineId, int quantity) throws OutOfStockException {
        for (Medicine m : medicineList) {
            if (m.getMedicineId().equals(medicineId)) {
                if (m.getStockQuantity() < quantity) {
                    throw new OutOfStockException("Insufficient stock for: " + m.getMedicineName()
                            + ". Requested: " + quantity + ", Available: " + m.getStockQuantity());
                }
                m.reduceStock(quantity);
                System.out.println("Dispensed " + quantity + " unit(s) of " + m.getMedicineName()
                        + ". Remaining stock: " + m.getStockQuantity());
                return;
            }
        }
        throw new OutOfStockException("Medicine not found in inventory: " + medicineId);
    }

    public ArrayList<Medicine> getMedicineList() { return medicineList; }
    public String getInventoryId() { return inventoryId; }
}