import java.time.LocalDate;

public class FitnessStepActivity extends Activity {
    private int steps;
    private double caloriesBurned;

    public FitnessStepActivity(String activityId, LocalDate activityDate, String description,
                               int steps, double caloriesBurned) {
        super(activityId, activityDate, description);

        if (steps < 0) {
            throw new IllegalArgumentException("Steps cannot be negative.");
        }

        if (caloriesBurned < 0) {
            throw new IllegalArgumentException("Calories burned cannot be negative.");
        }

        this.steps = steps;

        if (caloriesBurned == 0) {
            this.caloriesBurned = calculateCalories();
        } else {
            this.caloriesBurned = caloriesBurned;
        }

        this.pointsEarned = calculatePoints();
    }

    public double calculateCalories() {
        return steps * 0.04;
    }

    @Override
    public int calculatePoints() {
        return (int) (caloriesBurned / 10);
    }

    @Override
    public void displayActivityDetails() {
        super.displayActivityDetails();
        System.out.println("Steps: " + steps);
        System.out.println("Calories Burned: " + caloriesBurned);
        System.out.println();
    }

    public int getSteps() {
        return steps;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }
}
