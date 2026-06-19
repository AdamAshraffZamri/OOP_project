import java.util.ArrayList;

public class MedicineInventory {
    private String inventoryId ;
    private ArrayList <Medicine> medicineList ;

    public MedicineInventory( String inventoryId){
        this.inventoryId = inventoryId ;
        this.medicineList = new ArrayList<>() ; 
    }

    public String getInventoryId(){ 
        return inventoryId ;
    }

    public void addMedicine (Medicine medicine){
        medicineList.add(medicine);
    }

    public void removeMedicine (String medicineId){
        medicineList.removeIf( m -> m.getMedicineId().equals(medicineId)) ;
    }

    public void updateStock (String medicineId, int quantity){
        for (Medicine m : medicineList){
            if (m.getMedicineId().equals(medicineId)){
                m.increaseStock(quantity);
                return ;
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
        if (quantity <= 0) {
            throw new IllegalArgumentException("Dispense quantity must be greater than 0.");
        }

        for (Medicine m : medicineList) {
            if (m.getMedicineId().equals(medicineId)) {
                if (m.getStockQuantity() < quantity) {
                    throw new OutOfStockException("Insufficient stock for " + m.getMedicineName()
                            + ". Requested: " + quantity + ", Available: " + m.getStockQuantity());
                }
                m.reduceStock(quantity);
                System.out.println("Medicine dispensed successfully.");
                System.out.println("Medicine: " + m.getMedicineName());
                System.out.println("Quantity dispensed: " + quantity);
                System.out.println("Remaining stock: " + m.getStockQuantity());
                return;
            }
        }
        throw new OutOfStockException("Medicine not found in inventory: " + medicineId);
    }

    public ArrayList<Medicine> getMedicineList() { 
        return medicineList; 
    }

    public void display() {
        System.out.println("=== Medicine Inventory ===");
        if (medicineList.isEmpty()) {
            System.out.println("No medicine available.");
            return;
        }

        System.out.printf("%-12s %-24s %-10s %-14s %-10s%n", "ID", "Name", "Stock", "Expiry Date", "Status");
        System.out.println("------------------------------------------------------------------------");
        for (Medicine medicine : medicineList) {
            System.out.printf("%-12s %-24s %-10d %-14s %-10s%n",
                    medicine.getMedicineId(),
                    medicine.getMedicineName(),
                    medicine.getStockQuantity(),
                    medicine.getExpiryDate(),
                    medicine.isExpired() ? "Expired" : "Valid");
        }
    }
        
}    
