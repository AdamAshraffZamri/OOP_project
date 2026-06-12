import java.time.LocalDate;

public class RecyclingActivity extends Activity {
    private String materialType;
    private double weightInGram;

    public RecyclingActivity(String activityId, LocalDate activityDate, String description,
                             String materialType, double weightInGram) {
        super(activityId, activityDate, description);

        if (materialType == null || materialType.isBlank()) {
            throw new IllegalArgumentException("Material type cannot be empty.");
        }

        if (weightInGram < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }

        this.materialType = materialType;
        this.weightInGram = weightInGram;
        this.pointsEarned = calculatePoints();
    }

    @Override
    public int calculatePoints() {
        return (int) (weightInGram / 100);
    }

    @Override
    public void displayActivityDetails() {
        super.displayActivityDetails();
        System.out.println("Material Type: " + materialType);
        System.out.println("Weight: " + weightInGram + "g");
        System.out.println();
    }

    public String getMaterialType() {
        return materialType;
    }

    public double getWeightInGram() {
        return weightInGram;
    }

    public void setWeightInGram(double weightInGram) {
        if (weightInGram < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }

        this.weightInGram = weightInGram;
        this.pointsEarned = calculatePoints();
    }
}
