//! temporary cuz i need this to test my module -- Noah
import java.time.LocalDate;

public class Medicine {
    private String medicineId;
    private String medicineName;
    private int stockQuantity;
    private LocalDate expiryDate;

    public Medicine(String medicineId, String medicineName, int stockQuantity, LocalDate expiryDate) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.stockQuantity = stockQuantity;
        this.expiryDate = expiryDate;
    }

    public void increaseStock(int quantity) {
        if (quantity > 0) {
            this.stockQuantity += quantity;
        }
    }

    public void reduceStock(int quantity) {
        if (quantity > 0 && quantity <= stockQuantity) {
            this.stockQuantity -= quantity;
        }
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public String getMedicineId() { return medicineId; }
    public String getMedicineName() { return medicineName; }
    public int getStockQuantity() { return stockQuantity; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setStockQuantity(int qty) { this.stockQuantity = qty; }

    @Override
    public String toString() {
        return medicineId + "," + medicineName + "," + stockQuantity + "," + expiryDate;
    }
}