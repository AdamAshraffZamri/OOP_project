public class PointAccount {
    private String accountId;
    private int totalPoints;

    // Constructor
    public PointAccount(String accountId) {
        this.accountId = accountId;
        this.totalPoints = 0; // Everyone starts with 0 points
    }

    // Adds points to the total when an activity is completed
    public void addPoints(int points) {
        if (points > 0) {
            this.totalPoints += points;
        }
    }

    // Deducts points when a reward is redeemed
    public boolean deductPoints(int points) {
        if (hasEnoughPoints(points)) {
            this.totalPoints -= points;
            return true; // Deduction successful
        } else {
            return false; // Deduction failed
        }
    }

    // Helper method to check if the user can afford a reward
    public boolean hasEnoughPoints(int requiredPoints) {
        return this.totalPoints >= requiredPoints;
    }

    // Getter
    public int getTotalPoints() {
        return totalPoints;
    }
}
