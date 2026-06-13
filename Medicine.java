import java.time.LocalDate;

public class Medicine{
    private String medicineId;
    private String medicineName;
    private int stockQuantity;
    private LocalDate expiryDate;

    public Medicine (String medicineId, String medicineName, int stockQuantity, LocalDate expiryDate ){
        this.medicineId = medicineId;
        this.medicineName = medicineName ;
        this.stockQuantity = stockQuantity;
        this.expiryDate= expiryDate;
    } 

    public void increaseStock (int quantity){
        this.stockQuantity += quantity ;
    } 

    public void reduceStock (int quantity ){
        if (quantity > this.stockQuantity){
            this.stockQuantity = 0;
        }
        else {
            this.stockQuantity -= quantity ;
        }
    }

    public boolean isExpired (){
        return expiryDate.isBefore(LocalDate.now());
    }

    public String getMedicine(){
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
}

//{}[]