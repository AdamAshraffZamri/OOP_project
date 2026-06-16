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

    public int checkStock (String medicineId){
        for (Medicine m : medicineList){
            if (m.getMedicineId().equals(medicineId)){
                return m.getStockQuantity();
            }            
        }
        return 0 ;
    }

    public void dispenseMedicine (String medicineId, int quantity) throws OutOfStockException{
        for (Medicine m : medicineList ){
            if (m.getMedicineId().equals(medicineId)){
                if (m.getStockQuantity() == 0){
                    throw new OutOfStockException("Medicine " + m.getMedicineName() + " is out of stock.") ; 
                }
                if (m.getStockQuantity() < quantity){
                    throw new OutOfStockException("Not enough stock for the medicine " + m.getMedicineId()) ; 
                }
                m.reduceStock(quantity);
                return ;
            }
        }
        throw new OutOfStockException("Medicine ID " + medicineId + " not found.");
    }

    public ArrayList <Medicine> getMedicineList(){
        return medicineList ;
    }

    public String inventoryId (){
        return inventoryId ;
    }
}
