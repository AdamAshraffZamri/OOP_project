import java.time.LocalDate;

public class Medicine{
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
    public void increaseStock (int quantity){
        if (quantity > 0) {
            this.stockQuantity += quantity ;
        }
    } 

    public void reduceStock (int quantity ){
        if (quantity > 0 && quantity <= this.stockQuantity){
            this.stockQuantity -= quantity ;
        }
    }

    public boolean isExpired (){
        return expiryDate.isBefore(LocalDate.now());
    }

    public String getMedicineId(){
        return medicineId;
    } 

    public String getMedicineName(){
        return medicineName ;
    } 

    public int getStockQuantity(){
        return stockQuantity ;
    } 

    public LocalDate getExpiryDate(){
        return expiryDate ;
    }

    public void display() {
        System.out.println();
        System.out.println("Medicine Details");
        System.out.println("----------------");
        System.out.println("Medicine ID : " + medicineId);
        System.out.println("Name        : " + medicineName);
        System.out.println("Stock       : " + stockQuantity);
        System.out.println("Expiry Date : " + expiryDate);
        System.out.println("Status      : " + (isExpired() ? "Expired" : "Valid"));
    }
}

