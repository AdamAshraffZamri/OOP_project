import java.time.LocalDate;

public abstract class Activity {
    protected String activityId;
    protected LocalDate activityDate;
    protected String description;
    protected int pointsEarned;

    public Activity(String activityId, LocalDate activityDate, String description) {
        if (activityId == null || activityId.isBlank()) {
            throw new IllegalArgumentException("Activity ID cannot be empty.");
        }

        if (activityDate == null) {
            throw new IllegalArgumentException("Activity date cannot be null.");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }

        this.activityId = activityId;
        this.activityDate = activityDate;
        this.description = description;
        this.pointsEarned = 0;
    }

    public abstract int calculatePoints();

    public void displayActivityDetails() {
        System.out.println("Activity Type: " + getClass().getSimpleName());
        System.out.println("Activity ID: " + activityId);
        System.out.println("Date: " + activityDate);
        System.out.println("Description: " + description);
        System.out.println("Points Earned: " + pointsEarned);
    }

    public String getActivityId() {
        return activityId;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public String getDescription() {
        return description;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }
}