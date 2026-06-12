import java.time.LocalDate;

public class TradeActivity extends Activity {
    private String itemName;
    private double itemValue;
    private String tradeStatus;

    public TradeActivity(String activityId, LocalDate activityDate, String description,
                         String itemName, double itemValue) {
        super(activityId, activityDate, description);

        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be empty.");
        }

        if (itemValue < 0) {
            throw new IllegalArgumentException("Item value cannot be negative.");
        }

        this.itemName = itemName;
        this.itemValue = itemValue;
        this.tradeStatus = "Pending";
        this.pointsEarned = calculatePoints();
    }

    @Override
    public int calculatePoints() {
        return (int) (itemValue / 2);
    }

    @Override
    public void displayActivityDetails() {
        super.displayActivityDetails();
        System.out.println("Item Name: " + itemName);
        System.out.println("Item Value: RM" + itemValue);
        System.out.println("Trade Status: " + tradeStatus);
        System.out.println();
    }

    public void updateTradeStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Trade status cannot be empty.");
        }

        this.tradeStatus = status;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemValue() {
        return itemValue;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }
}